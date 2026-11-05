package com.example.appmobileenglishtrick.ui.screens

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.appmobileenglishtrick.ui.theme.AppMobileEnglishTrickTheme
import com.example.appmobileenglishtrick.R
import com.example.appmobileenglishtrick.audio.EnglishTtsManager
import com.example.appmobileenglishtrick.audio.SpeechInputManager
import com.example.appmobileenglishtrick.data.model.DifficultyLevel
import com.example.appmobileenglishtrick.data.model.QuestionReview
import com.example.appmobileenglishtrick.data.model.SpeakingQuestion
import com.example.appmobileenglishtrick.data.model.TestCategory
import com.example.appmobileenglishtrick.data.model.TestResult
import com.example.appmobileenglishtrick.data.repository.TestRepository
import com.example.appmobileenglishtrick.ui.components.TestProgressHeader
import com.example.appmobileenglishtrick.ui.theme.AccentAmber
import com.example.appmobileenglishtrick.ui.theme.AccentOrange
import com.example.appmobileenglishtrick.ui.theme.CardBorderLight
import com.example.appmobileenglishtrick.ui.theme.LevelEasy
import com.example.appmobileenglishtrick.ui.theme.LevelHard
import com.example.appmobileenglishtrick.ui.theme.LevelMedium
import com.example.appmobileenglishtrick.ui.theme.LogoCyan
import com.example.appmobileenglishtrick.ui.theme.LogoPink
import com.example.appmobileenglishtrick.ui.theme.PrimaryBlue
import com.example.appmobileenglishtrick.ui.theme.PrimaryBlueDark

@Composable
fun SpeakingTestScreen(
    level: DifficultyLevel,
    ttsManager: EnglishTtsManager? = null,
    onTestFinished: (TestResult) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isInspection = LocalInspectionMode.current
    val speechInputManager = remember(isInspection) {
        if (!isInspection) {
            try {
                SpeechInputManager(context)
            } catch (_: Exception) {
                null
            }
        } else null
    }

    val questions = remember(level) {
        val loaded = TestRepository.getQuestions(TestCategory.SPEAKING, level).filterIsInstance<SpeakingQuestion>()
        if (loaded.isNotEmpty()) loaded else getSampleSpeakingQuestions(level)
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    var isListening by remember { mutableStateOf(false) }
    var spokenText by remember { mutableStateOf("") }
    var accuracyScore by remember { mutableStateOf<Int?>(null) }
    var statusMessage by remember { mutableStateOf("Tekan mikrofon lalu ucapkan kalimat target.") }

    val userReviews = remember { mutableStateListOf<QuestionReview>() }
    var totalScoreSum by remember { mutableIntStateOf(0) }

    if (questions.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "No questions available.")
        }
        return
    }

    val currentQuestion = questions[currentIndex]

    // 1. System Google Voice Intent Launcher
    val systemVoiceLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        if (activityResult.resultCode == Activity.RESULT_OK) {
            val results = activityResult.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val text = results?.firstOrNull() ?: ""
            if (text.isNotBlank()) {
                spokenText = text
                val score = SpeechInputManager.calculateAccuracy(currentQuestion.targetSentence, text)
                accuracyScore = score
                statusMessage = if (score >= 75) "Pelafalan sangat bagus! 🎉" else "Suara terdeteksi! Akurasi: $score%"
            } else {
                statusMessage = "Tidak ada teks suara yang terdeteksi dari dialog."
            }
        }
    }

    // 2. Permission Launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted && speechInputManager != null) {
            startSpeaking(
                manager = speechInputManager,
                question = currentQuestion,
                onListeningChanged = { isListening = it },
                onSpoken = { spokenText = it },
                onAccuracy = { accuracyScore = it },
                onStatus = { statusMessage = it }
            )
        } else {
            statusMessage = "Izin mikrofon ditolak. Aktifkan izin audio di Pengaturan HP Anda."
        }
    }

    DisposableEffect(speechInputManager, ttsManager) {
        onDispose {
            speechInputManager?.stopListening()
            ttsManager?.stop()
        }
    }

    SpeakingTestContent(
        level = level,
        currentIndex = currentIndex,
        totalQuestions = questions.size,
        currentQuestion = currentQuestion,
        isListening = isListening,
        spokenText = spokenText,
        accuracyScore = accuracyScore,
        statusMessage = statusMessage,
        onPlaySampleAudio = {
            ttsManager?.speak(currentQuestion.targetSentence, 1.0f)
        },
        onMicClick = {
            if (isListening) {
                speechInputManager?.stopListening()
                isListening = false
                if (speechInputManager == null) {
                    spokenText = currentQuestion.targetSentence
                    accuracyScore = 92
                    statusMessage = "Pelafalan sangat bagus! 🎉 (92%)"
                }
            } else {
                if (speechInputManager != null) {
                    val hasPermission = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.RECORD_AUDIO
                    ) == PackageManager.PERMISSION_GRANTED

                    if (hasPermission) {
                        startSpeaking(
                            manager = speechInputManager,
                            question = currentQuestion,
                            onListeningChanged = { isListening = it },
                            onSpoken = { spokenText = it },
                            onAccuracy = { accuracyScore = it },
                            onStatus = { statusMessage = it }
                        )
                    } else {
                        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    }
                } else {
                    // Preview interactive mode simulation
                    isListening = true
                    statusMessage = "Mendengarkan suara Anda... (Simulasi)"
                }
            }
        },
        onOpenSystemVoiceDialog = {
            speechInputManager?.stopListening()
            isListening = false
            if (!isInspection) {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
                    putExtra(RecognizerIntent.EXTRA_PROMPT, "Ucapkan: ${currentQuestion.targetSentence}")
                }
                try {
                    systemVoiceLauncher.launch(intent)
                } catch (e: Exception) {
                    statusMessage = "Dialog suara sistem tidak didukung di perangkat ini."
                }
            } else {
                spokenText = currentQuestion.targetSentence
                accuracyScore = 90
                statusMessage = "Suara terdeteksi! Akurasi: 90% (Simulasi)"
            }
        },
        onSubmitAndNext = {
            speechInputManager?.stopListening()
            ttsManager?.stop()

            val score = accuracyScore ?: 0
            totalScoreSum += score
            userReviews.add(
                QuestionReview(
                    questionNumber = currentIndex + 1,
                    prompt = currentQuestion.targetSentence,
                    userAnswer = spokenText.ifBlank { "(Tidak ada suara terdeteksi)" },
                    correctAnswer = currentQuestion.targetSentence,
                    isCorrect = score >= 60,
                    explanation = "Skor Akurasi: $score%. Tips: ${currentQuestion.tip}"
                )
            )

            if (currentIndex < questions.size - 1) {
                currentIndex++
                spokenText = ""
                accuracyScore = null
                isListening = false
                statusMessage = "Tekan mikrofon lalu ucapkan kalimat target."
            } else {
                val avgScore = (totalScoreSum.toFloat() / questions.size.toFloat()).toInt()
                val correctCount = userReviews.count { it.isCorrect }
                onTestFinished(
                    TestResult(
                        category = TestCategory.SPEAKING,
                        level = level,
                        score = avgScore,
                        totalQuestions = questions.size,
                        correctCount = correctCount,
                        reviews = userReviews.toList()
                    )
                )
            }
        },
        onBackClick = {
            speechInputManager?.stopListening()
            ttsManager?.stop()
            onBackClick()
        },
        modifier = modifier
    )
}

@Composable
fun SpeakingTestContent(
    level: DifficultyLevel,
    currentIndex: Int,
    totalQuestions: Int,
    currentQuestion: SpeakingQuestion,
    isListening: Boolean,
    spokenText: String,
    accuracyScore: Int?,
    statusMessage: String,
    onPlaySampleAudio: () -> Unit,
    onMicClick: () -> Unit,
    onOpenSystemVoiceDialog: () -> Unit,
    onSubmitAndNext: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    // Mic Pulsing Animation
    val infiniteTransition = rememberInfiniteTransition(label = "micPulse")
    val micScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isListening) 1.25f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "micScale"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Progress Header
        TestProgressHeader(
            category = TestCategory.SPEAKING,
            level = level,
            currentIndex = currentIndex,
            totalQuestions = totalQuestions,
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Context & Slogan Card with Official Logo
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = LogoCyan.copy(alpha = 0.08f)
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, LogoCyan.copy(alpha = 0.35f))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_english_trick),
                        contentDescription = "Logo English Trick",
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "ENGLISH TRICK • NO TRICK, NO SPEAK",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = LogoPink
                        )
                        Text(
                            text = currentQuestion.context,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            // Target Sentence Display Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, PrimaryBlue.copy(alpha = 0.25f))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Target Phrase to Speak",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = PrimaryBlue
                        )

                        // Listen Sample Native Pronunciation Button
                        Surface(
                            color = PrimaryBlue.copy(alpha = 0.08f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.clickable {
                                onPlaySampleAudio()
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "🔊 Contoh Suara", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = PrimaryBlue)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "“${currentQuestion.targetSentence}”",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        lineHeight = 25.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "IPA: ${currentQuestion.phoneticGuide}",
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "💡 Tips: ${currentQuestion.tip}",
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                    )
                }
            }

            // Interactive Microphone Control Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderLight)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Status text with friendly Indonesian explanations
                    Text(
                        text = statusMessage,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isListening) AccentOrange else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Microphone Circle Button
                    Surface(
                        color = if (isListening) AccentOrange else PrimaryBlue,
                        shape = CircleShape,
                        shadowElevation = 6.dp,
                        modifier = Modifier
                            .size(72.dp)
                            .scale(micScale)
                            .clickable {
                                onMicClick()
                            }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = if (isListening) "⏹" else "🎙️",
                                fontSize = 30.sp,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (isListening) "Mendengarkan... Silakan bicara!" else "Ketuk mic untuk bicara langsung",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isListening) AccentOrange else PrimaryBlue
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Opsi Dialog Suara Google Sistem (Sangat handal di semua HP & Emulator)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        OutlinedButton(
                            onClick = onOpenSystemVoiceDialog,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.5f))
                        ) {
                            Text(text = "📢 Buka Dialog Suara Google", fontSize = 12.sp, color = PrimaryBlue)
                        }
                    }

                    // Spoken Result & Accuracy Meter
                    if (spokenText.isNotBlank()) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Surface(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Hasil Ucapan Anda:",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )

                                    accuracyScore?.let { score ->
                                        val badgeColor = when {
                                            score >= 75 -> LevelEasy
                                            score >= 50 -> LevelMedium
                                            else -> LevelHard
                                        }
                                        Surface(
                                            color = badgeColor.copy(alpha = 0.15f),
                                            shape = RoundedCornerShape(6.dp),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, badgeColor)
                                        ) {
                                            Text(
                                                text = "$score% Akurasi",
                                                color = badgeColor,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "“$spokenText”",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }

        // Bottom Action Bar
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                Button(
                    onClick = onSubmitAndNext,
                    enabled = spokenText.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    Text(
                        text = if (currentIndex < totalQuestions - 1) "Konfirmasi & Lanjut Soal Berikutnya ➔" else "Lihat Hasil Speaking 🏆",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

private fun startSpeaking(
    manager: SpeechInputManager,
    question: SpeakingQuestion?,
    onListeningChanged: (Boolean) -> Unit,
    onSpoken: (String) -> Unit,
    onAccuracy: (Int) -> Unit,
    onStatus: (String) -> Unit
) {
    if (question == null) return
    onStatus("Mendengarkan suara Anda... Silakan bicara.")
    manager.startListening(
        onResult = { result ->
            onSpoken(result)
            val score = SpeechInputManager.calculateAccuracy(question.targetSentence, result)
            onAccuracy(score)
            onStatus(if (score >= 75) "Pelafalan sangat bagus! 🎉 ($score%)" else "Suara terdeteksi! Akurasi: $score%")
        },
        onError = { err ->
            onStatus(err)
        },
        onListeningStateChanged = { listening ->
            onListeningChanged(listening)
        }
    )
}

// Sample mock data for Previews and Fallback
private fun getSampleSpeakingQuestions(level: DifficultyLevel): List<SpeakingQuestion> = listOf(
    SpeakingQuestion(
        id = "mock_s_1",
        level = level,
        title = "Self Introduction",
        context = "Percakapan perkenalan diri dalam lingkungan profesional",
        targetSentence = "I am delighted to meet everyone on the team today.",
        phoneticGuide = "/aɪ æm dɪˈlaɪtɪd tuː miːt ˈɛvrɪwʌn ɒn ðə tiːm təˈdeɪ/",
        tip = "Ucapkan kata 'delighted' dengan penekanan pada suku kata kedua."
    )
)

// ==========================================
// PREVIEWS
// ==========================================

@Preview(showBackground = true, name = "Speaking Test - Interactive Mode")
@Composable
private fun SpeakingTestInteractivePreview() {
    AppMobileEnglishTrickTheme {
        SpeakingTestScreen(
            level = DifficultyLevel.EASY,
            ttsManager = null,
            onTestFinished = {},
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Speaking Test Content - Initial State")
@Composable
private fun SpeakingTestContentInitialPreview() {
    AppMobileEnglishTrickTheme {
        SpeakingTestContent(
            level = DifficultyLevel.EASY,
            currentIndex = 0,
            totalQuestions = 3,
            currentQuestion = getSampleSpeakingQuestions(DifficultyLevel.EASY)[0],
            isListening = false,
            spokenText = "",
            accuracyScore = null,
            statusMessage = "Tekan mikrofon lalu ucapkan kalimat target.",
            onPlaySampleAudio = {},
            onMicClick = {},
            onOpenSystemVoiceDialog = {},
            onSubmitAndNext = {},
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Speaking Test Content - High Accuracy State")
@Composable
private fun SpeakingTestContentHighAccuracyPreview() {
    AppMobileEnglishTrickTheme {
        SpeakingTestContent(
            level = DifficultyLevel.MEDIUM,
            currentIndex = 1,
            totalQuestions = 4,
            currentQuestion = getSampleSpeakingQuestions(DifficultyLevel.MEDIUM)[0],
            isListening = false,
            spokenText = "I am delighted to meet everyone on the team today.",
            accuracyScore = 94,
            statusMessage = "Pelafalan sangat bagus! 🎉 (94%)",
            onPlaySampleAudio = {},
            onMicClick = {},
            onOpenSystemVoiceDialog = {},
            onSubmitAndNext = {},
            onBackClick = {}
        )
    }
}

