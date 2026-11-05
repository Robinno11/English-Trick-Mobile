package com.example.appmobileenglishtrick.audio

import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import java.util.Locale
import java.util.UUID

class EnglishTtsManager(context: Context) {

    private var tts: TextToSpeech? = null
    var isInitialized = false
        private set

    var onSpeakingStateChanged: ((Boolean) -> Unit)? = null

    init {
        initTts(context.applicationContext)
    }

    private fun initTts(appContext: Context) {
        // Priority 1: Check if Google Speech Services ("com.google.android.tts") is installed.
        // Google TTS provides high-definition, neural voices without crackling/static artifacts.
        val googleEngine = "com.google.android.tts"
        val isGoogleEngineAvailable = try {
            val intent = Intent(TextToSpeech.Engine.INTENT_ACTION_TTS_SERVICE).setPackage(googleEngine)
            val resolveInfos = appContext.packageManager.queryIntentServices(intent, 0)
            resolveInfos.isNotEmpty()
        } catch (e: Exception) {
            false
        }

        var isFallingBack = false

        val onInitListener = object : TextToSpeech.OnInitListener {
            override fun onInit(status: Int) {
                if (status == TextToSpeech.SUCCESS) {
                    configureTtsSettings()
                } else if (!isFallingBack) {
                    // Fallback to system default TTS if Google engine failed to initialize
                    isFallingBack = true
                    try {
                        tts?.shutdown()
                    } catch (_: Exception) {}
                    tts = TextToSpeech(appContext, this)
                }
            }
        }

        tts = if (isGoogleEngineAvailable) {
            TextToSpeech(appContext, onInitListener, googleEngine)
        } else {
            TextToSpeech(appContext, onInitListener)
        }
    }

    private fun configureTtsSettings() {
        val currentTts = tts ?: return

        // 1. Configure AudioAttributes for Speech
        // Directs audio output to speech-optimized filters and optimal buffer sizing, preventing jitter and crackle
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
            .build()
        currentTts.setAudioAttributes(audioAttributes)

        // 2. Set Language
        val langResult = currentTts.setLanguage(Locale.US)
        if (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
            currentTts.setLanguage(Locale.ENGLISH)
        }

        // 3. Select the smoothest, highest-fidelity voice available
        selectBestVoice(currentTts)

        // 4. Default natural human speech rate and pitch (1.0f prevents resampler flanging)
        currentTts.setSpeechRate(1.0f)
        currentTts.setPitch(1.0f)

        // 5. Utterance Progress Listener
        currentTts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                onSpeakingStateChanged?.invoke(true)
            }

            override fun onDone(utteranceId: String?) {
                onSpeakingStateChanged?.invoke(false)
            }

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) {
                onSpeakingStateChanged?.invoke(false)
            }

            override fun onError(utteranceId: String?, errorCode: Int) {
                onSpeakingStateChanged?.invoke(false)
            }
        })

        isInitialized = true
    }

    private fun selectBestVoice(ttsInstance: TextToSpeech) {
        try {
            val voices = ttsInstance.voices ?: return
            if (voices.isEmpty()) return

            // Filter for English voices that are installed locally
            val englishVoices = voices.filter { voice ->
                val lang = voice.locale.language.lowercase()
                (lang == "en") && !voice.features.contains(TextToSpeech.Engine.KEY_FEATURE_NOT_INSTALLED)
            }

            if (englishVoices.isEmpty()) return

            // Scoring system to pick the smoothest, non-crackly voice:
            // - Prefer higher quality (QUALITY_VERY_HIGH = 500, QUALITY_HIGH = 400)
            // - Avoid LATENCY_VERY_LOW (which uses aggressive downsampling/quantization)
            // - Prefer neural, natural, or high-definition models
            // - Prefer local installed voices over streaming network voices
            val bestVoice = englishVoices.maxWithOrNull(
                compareBy<Voice> { voice ->
                    voice.quality
                }.thenBy { voice ->
                    if (voice.latency == Voice.LATENCY_VERY_LOW) 0 else 1
                }.thenBy { voice ->
                    val name = voice.name.lowercase()
                    when {
                        name.contains("neural") || name.contains("natural") || name.contains("wavenet") -> 3
                        name.contains("local") && !name.contains("low") -> 2
                        !name.contains("low") -> 1
                        else -> 0
                    }
                }.thenBy { voice ->
                    if (voice.locale == Locale.US) 2 else if (voice.locale == Locale.UK) 1 else 0
                }
            )

            if (bestVoice != null) {
                ttsInstance.voice = bestVoice
            }
        } catch (_: Exception) {
            // Fallback gracefully to default voice
        }
    }

    fun speak(text: String, rate: Float = 1.0f) {
        if (!isInitialized) return
        val currentTts = tts ?: return

        currentTts.setSpeechRate(rate)

        val cleanedText = cleanTextForSpeech(text)
        val utteranceId = UUID.randomUUID().toString()

        // Set KEY_PARAM_VOLUME to 0.90f:
        // Backing off by ~1 dB prevents digital peak clipping on consonant bursts (s, t, p, k)
        // which is the primary cause of scratchy / crackling ("kresek-kresek") distortion.
        val params = Bundle().apply {
            putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 0.90f)
            putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId)
        }

        currentTts.speak(cleanedText, TextToSpeech.QUEUE_FLUSH, params, utteranceId)
    }

    private fun cleanTextForSpeech(text: String): String {
        return text
            // Dialogue prefix like "Barista:" or "Customer:" -> add natural conversational comma/pause
            .replace(Regex("""\b([A-Z][a-zA-Z\s]{1,20}):\s*"""), "$1, ")
            // Normalize dashes
            .replace(Regex("""--+"""), " — ")
            // Clean markdown/symbols that might cause audio clicks
            .replace(Regex("""[#*^~_]"""), " ")
            .replace(Regex("""\s+"""), " ")
            .trim()
    }

    fun stop() {
        tts?.stop()
        onSpeakingStateChanged?.invoke(false)
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        isInitialized = false
    }
}
