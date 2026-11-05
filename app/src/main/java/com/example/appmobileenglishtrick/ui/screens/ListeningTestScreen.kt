package com.example.appmobileenglishtrick.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.appmobileenglishtrick.ui.theme.AppMobileEnglishTrickTheme
import com.example.appmobileenglishtrick.audio.EnglishTtsManager
import com.example.appmobileenglishtrick.data.model.DifficultyLevel
import com.example.appmobileenglishtrick.data.model.ListeningQuestion
import com.example.appmobileenglishtrick.data.model.QuestionReview
import com.example.appmobileenglishtrick.data.model.TestCategory
import com.example.appmobileenglishtrick.data.model.TestResult
import com.example.appmobileenglishtrick.data.repository.TestRepository
import com.example.appmobileenglishtrick.ui.components.PulsingWaveIndicator
import com.example.appmobileenglishtrick.ui.components.TestProgressHeader
import com.example.appmobileenglishtrick.ui.theme.AccentAmber
import com.example.appmobileenglishtrick.ui.theme.AccentOrange
import com.example.appmobileenglishtrick.ui.theme.CardBorderLight
import com.example.appmobileenglishtrick.ui.theme.LevelEasy
import com.example.appmobileenglishtrick.ui.theme.LevelHard
import com.example.appmobileenglishtrick.ui.theme.PrimaryBlue
import com.example.appmobileenglishtrick.ui.theme.PrimaryBlueDark

@Composable
fun ListeningTestScreen(
    level: DifficultyLevel,
    ttsManager: EnglishTtsManager? = null,
    onTestFinished: (TestResult) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val questions = remember(level) {
        val loaded = TestRepository.getQuestions(TestCategory.LISTENING, level).filterIsInstance<ListeningQuestion>()
        if (loaded.isNotEmpty()) loaded else getSampleListeningQuestions(level)
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var hasAnswered by remember { mutableStateOf(false) }
    var isPlayingAudio by remember { mutableStateOf(false) }
    var speechRate by remember { mutableFloatStateOf(1.0f) }
    var showTranscript by remember { mutableStateOf(false) }

    val userReviews = remember { mutableStateListOf<QuestionReview>() }
    var correctCount by remember { mutableIntStateOf(0) }

    DisposableEffect(ttsManager) {
        ttsManager?.onSpeakingStateChanged = { playing ->
            isPlayingAudio = playing
        }
        onDispose {
            ttsManager?.stop()
        }
    }

    if (questions.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "No questions available.")
        }
        return
    }

    val currentQuestion = questions[currentIndex]

    ListeningTestContent(
        level = level,
        currentIndex = currentIndex,
        totalQuestions = questions.size,
        currentQuestion = currentQuestion,
        isPlayingAudio = isPlayingAudio,
        speechRate = speechRate,
        showTranscript = showTranscript,
        selectedOptionIndex = selectedOptionIndex,
        hasAnswered = hasAnswered,
        onPlayPauseClick = {
            if (isPlayingAudio) {
                ttsManager?.stop()
                isPlayingAudio = false
            } else {
                if (ttsManager != null) {
                    ttsManager.speak(currentQuestion.audioScript, speechRate)
                } else {
                    // Preview simulation mode
                    isPlayingAudio = true
                }
            }
        },
        onReplayClick = {
            ttsManager?.stop()
            if (ttsManager != null) {
                ttsManager.speak(currentQuestion.audioScript, speechRate)
            } else {
                isPlayingAudio = true
            }
        },
        onSpeechRateChange = { rate ->
            speechRate = rate
            if (isPlayingAudio && ttsManager != null) {
                ttsManager.stop()
                ttsManager.speak(currentQuestion.audioScript, speechRate)
            }
        },
        onToggleTranscript = {
            showTranscript = !showTranscript
        },
        onSelectOption = { index ->
            if (!hasAnswered) {
                selectedOptionIndex = index
            }
        },
        onSubmitAnswer = {
            if (selectedOptionIndex != null) {
                ttsManager?.stop()
                isPlayingAudio = false
                hasAnswered = true
                val isCorrect = selectedOptionIndex == currentQuestion.correctIndex
                if (isCorrect) correctCount++
                userReviews.add(
                    QuestionReview(
                        questionNumber = currentIndex + 1,
                        prompt = currentQuestion.questionText,
                        userAnswer = currentQuestion.options[selectedOptionIndex!!],
                        correctAnswer = currentQuestion.options[currentQuestion.correctIndex],
                        isCorrect = isCorrect,
                        explanation = currentQuestion.explanation
                    )
                )
            }
        },
        onNextQuestion = {
            ttsManager?.stop()
            isPlayingAudio = false
            if (currentIndex < questions.size - 1) {
                currentIndex++
                selectedOptionIndex = null
                hasAnswered = false
                showTranscript = false
            } else {
                val score = ((correctCount.toFloat() / questions.size.toFloat()) * 100).toInt()
                onTestFinished(
                    TestResult(
                        category = TestCategory.LISTENING,
                        level = level,
                        score = score,
                        totalQuestions = questions.size,
                        correctCount = correctCount,
                        reviews = userReviews.toList()
                    )
                )
            }
        },
        onBackClick = {
            ttsManager?.stop()
            isPlayingAudio = false
            onBackClick()
        },
        modifier = modifier
    )
}

@Composable
fun ListeningTestContent(
    level: DifficultyLevel,
    currentIndex: Int,
    totalQuestions: Int,
    currentQuestion: ListeningQuestion,
    isPlayingAudio: Boolean,
    speechRate: Float,
    showTranscript: Boolean,
    selectedOptionIndex: Int?,
    hasAnswered: Boolean,
    onPlayPauseClick: () -> Unit,
    onReplayClick: () -> Unit,
    onSpeechRateChange: (Float) -> Unit,
    onToggleTranscript: () -> Unit,
    onSelectOption: (Int) -> Unit,
    onSubmitAnswer: () -> Unit,
    onNextQuestion: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Progress Header
        TestProgressHeader(
            category = TestCategory.LISTENING,
            level = level,
            currentIndex = currentIndex,
            totalQuestions = totalQuestions,
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Audio Player Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, PrimaryBlue.copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = currentQuestion.audioTitle,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = PrimaryBlueDark
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Speaker: ${currentQuestion.audioSpeaker}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Animated Sound Wave
                    PulsingWaveIndicator(isPlaying = isPlayingAudio, modifier = Modifier.height(30.dp))

                    Spacer(modifier = Modifier.height(16.dp))

                    // Audio Action Buttons
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            color = if (isPlayingAudio) AccentOrange else PrimaryBlue,
                            shape = CircleShape,
                            modifier = Modifier
                                .size(60.dp)
                                .clickable { onPlayPauseClick() }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = if (isPlayingAudio) "⏸" else "▶",
                                    fontSize = 24.sp,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(18.dp))

                        // Replay Button
                        Surface(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                            shape = CircleShape,
                            modifier = Modifier
                                .size(44.dp)
                                .clickable { onReplayClick() }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = "↺", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Speed Toggles
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "Speed: ", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                        listOf(0.8f to "0.8x", 1.0f to "1.0x", 1.2f to "1.2x").forEach { (rate, label) ->
                            val isSelected = speechRate == rate
                            Surface(
                                color = if (isSelected) PrimaryBlue else Color.Transparent,
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .clickable { onSpeechRateChange(rate) }
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    // Transcript Toggle Button
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (showTranscript) "Hide Audio Transcript ▲" else "Show Audio Transcript ▼",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryBlue,
                        modifier = Modifier
                            .clickable { onToggleTranscript() }
                            .padding(6.dp)
                    )

                    if (showTranscript) {
                        Surface(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        ) {
                            Text(
                                text = currentQuestion.audioScript,
                                fontSize = 12.sp,
                                lineHeight = 18.sp,
                                modifier = Modifier.padding(12.dp),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
                            )
                        }
                    }
                }
            }

            // Question Text
            Text(
                text = "Question: ${currentQuestion.questionText}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                lineHeight = 22.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            // Multiple Choice Options
            val optionPrefixes = listOf("A", "B", "C", "D")
            currentQuestion.options.forEachIndexed { index, optionText ->
                val isSelected = selectedOptionIndex == index
                val isCorrect = index == currentQuestion.correctIndex

                val borderColor = when {
                    hasAnswered && isCorrect -> LevelEasy
                    hasAnswered && isSelected && !isCorrect -> LevelHard
                    isSelected -> PrimaryBlue
                    else -> CardBorderLight
                }

                val bgColor = when {
                    hasAnswered && isCorrect -> LevelEasy.copy(alpha = 0.1f)
                    hasAnswered && isSelected && !isCorrect -> LevelHard.copy(alpha = 0.1f)
                    isSelected -> PrimaryBlue.copy(alpha = 0.08f)
                    else -> MaterialTheme.colorScheme.surface
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = !hasAnswered) {
                            onSelectOption(index)
                        },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = bgColor),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, borderColor)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = if (isSelected) PrimaryBlue else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                            shape = CircleShape,
                            modifier = Modifier.size(30.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = optionPrefixes.getOrElse(index) { "$index" },
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = optionText,
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Explanation box if answered
            if (hasAnswered) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedOptionIndex == currentQuestion.correctIndex) {
                            LevelEasy.copy(alpha = 0.1f)
                        } else {
                            LevelHard.copy(alpha = 0.1f)
                        }
                    )
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = if (selectedOptionIndex == currentQuestion.correctIndex) "✓ Correct!" else "✕ Incorrect",
                            fontWeight = FontWeight.Bold,
                            color = if (selectedOptionIndex == currentQuestion.correctIndex) LevelEasy else LevelHard,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = currentQuestion.explanation,
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
                        )
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
                if (!hasAnswered) {
                    Button(
                        onClick = onSubmitAnswer,
                        enabled = selectedOptionIndex != null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                    ) {
                        Text(text = "Submit Answer", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                } else {
                    Button(
                        onClick = onNextQuestion,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                    ) {
                        Text(
                            text = if (currentIndex < totalQuestions - 1) "Next Question ➔" else "See Test Results 🏆",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}

// Sample mock data for Previews and Fallback
private fun getSampleListeningQuestions(level: DifficultyLevel): List<ListeningQuestion> = listOf(
    ListeningQuestion(
        id = "mock_l_1",
        level = level,
        audioTitle = "Airport Announcement",
        audioSpeaker = "Airport Staff",
        audioScript = "Attention all passengers on flight GA 420 to Tokyo Narita. Boarding is now commencing at gate B4.",
        questionText = "Which gate is flight GA 420 boarding at?",
        options = listOf("Gate A2", "Gate B4", "Gate C7", "Gate D1"),
        correctIndex = 1,
        explanation = "The announcement explicitly states boarding at gate B4."
    )
)

// ==========================================
// PREVIEWS
// ==========================================

@Preview(showBackground = true, name = "Listening Test - Interactive Mode")
@Composable
private fun ListeningTestInteractivePreview() {
    AppMobileEnglishTrickTheme {
        ListeningTestScreen(
            level = DifficultyLevel.EASY,
            ttsManager = null,
            onTestFinished = {},
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Listening Test Content - Playing Audio State")
@Composable
private fun ListeningTestContentPlayingPreview() {
    AppMobileEnglishTrickTheme {
        ListeningTestContent(
            level = DifficultyLevel.EASY,
            currentIndex = 0,
            totalQuestions = 3,
            currentQuestion = getSampleListeningQuestions(DifficultyLevel.EASY)[0],
            isPlayingAudio = true,
            speechRate = 1.0f,
            showTranscript = false,
            selectedOptionIndex = null,
            hasAnswered = false,
            onPlayPauseClick = {},
            onReplayClick = {},
            onSpeechRateChange = {},
            onToggleTranscript = {},
            onSelectOption = {},
            onSubmitAnswer = {},
            onNextQuestion = {},
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Listening Test Content - Answered State")
@Composable
private fun ListeningTestContentAnsweredPreview() {
    AppMobileEnglishTrickTheme {
        ListeningTestContent(
            level = DifficultyLevel.MEDIUM,
            currentIndex = 1,
            totalQuestions = 4,
            currentQuestion = getSampleListeningQuestions(DifficultyLevel.MEDIUM)[0],
            isPlayingAudio = false,
            speechRate = 1.0f,
            showTranscript = true,
            selectedOptionIndex = 1,
            hasAnswered = true,
            onPlayPauseClick = {},
            onReplayClick = {},
            onSpeechRateChange = {},
            onToggleTranscript = {},
            onSelectOption = {},
            onSubmitAnswer = {},
            onNextQuestion = {},
            onBackClick = {}
        )
    }
}

