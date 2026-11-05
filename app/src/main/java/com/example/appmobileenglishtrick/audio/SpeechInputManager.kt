package com.example.appmobileenglishtrick.audio

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import java.util.Locale
import kotlin.math.max

class SpeechInputManager(private val context: Context) {

    private var speechRecognizer: SpeechRecognizer? = null

    fun isRecognitionAvailable(): Boolean {
        return SpeechRecognizer.isRecognitionAvailable(context)
    }

    fun startListening(
        onResult: (String) -> Unit,
        onError: (String) -> Unit,
        onListeningStateChanged: (Boolean) -> Unit
    ) {
        if (!isRecognitionAvailable()) {
            onError("Layanan pengenal suara Google tidak tersedia di perangkat ini. Gunakan tombol Dialog Suara Sistem di bawah.")
            return
        }

        stopListening()

        try {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                setRecognitionListener(object : RecognitionListener {
                    override fun onReadyForSpeech(params: Bundle?) {
                        onListeningStateChanged(true)
                    }

                    override fun onBeginningOfSpeech() {}
                    override fun onRmsChanged(rmsdB: Float) {}
                    override fun onBufferReceived(buffer: ByteArray?) {}

                    override fun onEndOfSpeech() {
                        onListeningStateChanged(false)
                    }

                    override fun onError(error: Int) {
                        onListeningStateChanged(false)
                        val message = getFriendlyErrorMessage(error)
                        onError(message)
                    }

                    override fun onResults(results: Bundle?) {
                        onListeningStateChanged(false)
                        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        val spokenText = matches?.firstOrNull() ?: ""
                        if (spokenText.isNotBlank()) {
                            onResult(spokenText)
                        } else {
                            onError("Suara tidak tertangkap jelas. Silakan coba lagi atau gunakan Dialog Suara.")
                        }
                    }

                    override fun onPartialResults(partialResults: Bundle?) {}
                    override fun onEvent(eventType: Int, params: Bundle?) {}
                })
            }

            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en-US")
                putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, "en-US")
                putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false)
                putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
            }

            speechRecognizer?.startListening(intent)
        } catch (e: Exception) {
            onListeningStateChanged(false)
            onError("Gagal mengaktifkan mikrofon (${e.localizedMessage}). Coba tombol Dialog Suara Sistem.")
        }
    }

    fun stopListening() {
        try {
            speechRecognizer?.stopListening()
            speechRecognizer?.cancel()
            speechRecognizer?.destroy()
        } catch (_: Exception) {}
        speechRecognizer = null
    }

    companion object {
        fun getFriendlyErrorMessage(error: Int): String {
            return when (error) {
                SpeechRecognizer.ERROR_AUDIO ->
                    "Gangguan audio/mikrofon. Pastikan mikrofon berfungsi dengan baik."
                SpeechRecognizer.ERROR_CLIENT ->
                    "Layanan suara belum siap atau sibuk. Silakan coba tekan mic lagi."
                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS ->
                    "Izin mikrofon diperlukan. Harap izinkan perekaman suara pada aplikasi."
                SpeechRecognizer.ERROR_NETWORK ->
                    "Koneksi internet tidak stabil untuk Google Voice. Coba gunakan tombol 'Dialog Suara Google'."
                SpeechRecognizer.ERROR_NETWORK_TIMEOUT ->
                    "Koneksi server suara waktu habis. Silakan coba bicara kembali."
                SpeechRecognizer.ERROR_NO_MATCH ->
                    "Suara belum terdeteksi jelas. Silakan dekatkan suara ke mikrofon dan ucapkan lagi."
                SpeechRecognizer.ERROR_RECOGNIZER_BUSY ->
                    "Layanan suara sedang sibuk. Tunggu sejenak lalu tekan mikrofon kembali."
                SpeechRecognizer.ERROR_SERVER ->
                    "Server suara Google sedang mengalami kendala. Gunakan tombol 'Dialog Suara Google'."
                SpeechRecognizer.ERROR_SPEECH_TIMEOUT ->
                    "Waktu bicara habis tanpa input suara. Silakan tekan mikrofon lalu segera bicara."
                10 -> // ERROR_TOO_MANY_REQUESTS
                    "Terlalu banyak permintaan suara dalam waktu singkat. Tunggu 2 detik."
                11 -> // ERROR_SERVER_DISCONNECTED
                    "Server suara terputus. Silakan coba lagi."
                12, 13 -> // ERROR_LANGUAGE_NOT_SUPPORTED / UNAVAILABLE
                    "Paket bahasa Inggris belum terunduh di perangkat. Gunakan tombol 'Dialog Suara Google'."
                else ->
                    "Suara tidak tertangkap (status $error). Silakan tekan tombol mic atau gunakan Dialog Suara."
            }
        }

        /**
         * Calculates speech pronunciation accuracy score (0 to 100%)
         * comparing spoken text against target phrase.
         */
        fun calculateAccuracy(target: String, spoken: String): Int {
            if (spoken.isBlank()) return 0

            val cleanTarget = normalizeText(target)
            val cleanSpoken = normalizeText(spoken)

            if (cleanTarget == cleanSpoken) return 100

            val targetWords = cleanTarget.split("\\s+".toRegex()).filter { it.isNotBlank() }
            val spokenWords = cleanSpoken.split("\\s+".toRegex()).filter { it.isNotBlank() }

            if (targetWords.isEmpty()) return 0

            // Word overlap score
            var matchedWords = 0
            val spokenPool = spokenWords.toMutableList()
            for (tw in targetWords) {
                val matchIdx = spokenPool.indexOfFirst { sw ->
                    sw == tw || levenshteinDistance(sw, tw) <= 1
                }
                if (matchIdx != -1) {
                    matchedWords++
                    spokenPool.removeAt(matchIdx)
                }
            }

            val wordScore = (matchedWords.toFloat() / targetWords.size.toFloat()) * 100f

            // Character level similarity
            val maxLen = max(cleanTarget.length, cleanSpoken.length)
            val dist = levenshteinDistance(cleanTarget, cleanSpoken)
            val charScore = ((maxLen - dist).toFloat() / maxLen.toFloat()).coerceIn(0f, 1f) * 100f

            // Blended accuracy
            val finalScore = (wordScore * 0.7f + charScore * 0.3f).toInt()
            return finalScore.coerceIn(0, 100)
        }

        private fun normalizeText(text: String): String {
            return text.lowercase(Locale.ROOT)
                .replace("[^a-z0-9\\s]".toRegex(), "")
                .trim()
        }

        private fun levenshteinDistance(s1: String, s2: String): Int {
            val dp = Array(s1.length + 1) { IntArray(s2.length + 1) }
            for (i in 0..s1.length) dp[i][0] = i
            for (j in 0..s2.length) dp[0][j] = j

            for (i in 1..s1.length) {
                for (j in 1..s2.length) {
                    val cost = if (s1[i - 1] == s2[j - 1]) 0 else 1
                    dp[i][j] = minOf(
                        dp[i - 1][j] + 1,
                        dp[i][j - 1] + 1,
                        dp[i - 1][j - 1] + cost
                    )
                }
            }
            return dp[s1.length][s2.length]
        }
    }
}
