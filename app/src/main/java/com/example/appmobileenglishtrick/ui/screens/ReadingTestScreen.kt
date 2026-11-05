package com.example.appmobileenglishtrick.ui.screens

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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.appmobileenglishtrick.data.model.DifficultyLevel
import com.example.appmobileenglishtrick.data.model.QuestionReview
import com.example.appmobileenglishtrick.data.model.ReadingQuestion
import com.example.appmobileenglishtrick.data.model.TestCategory
import com.example.appmobileenglishtrick.data.model.TestResult
import com.example.appmobileenglishtrick.data.repository.TestRepository
import com.example.appmobileenglishtrick.ui.components.TestProgressHeader
import com.example.appmobileenglishtrick.ui.theme.CardBorderLight
import com.example.appmobileenglishtrick.ui.theme.LevelEasy
import com.example.appmobileenglishtrick.ui.theme.LevelHard
import com.example.appmobileenglishtrick.ui.theme.PrimaryBlue
import com.example.appmobileenglishtrick.ui.theme.PrimaryBlueDark

@Composable
fun ReadingTestScreen(
    level: DifficultyLevel,
    onTestFinished: (TestResult) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val questions = remember(level) {
        val loaded = TestRepository.getQuestions(TestCategory.READING, level).filterIsInstance<ReadingQuestion>()
        if (loaded.isNotEmpty()) loaded else getSampleReadingQuestions(level)
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var hasAnswered by remember { mutableStateOf(false) }

    val userReviews = remember { mutableStateListOf<QuestionReview>() }
    var correctCount by remember { mutableIntStateOf(0) }

    if (questions.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "No questions available.")
        }
        return
    }

    val currentQuestion = questions[currentIndex]

    ReadingTestContent(
        level = level,
        currentIndex = currentIndex,
        totalQuestions = questions.size,
        currentQuestion = currentQuestion,
        selectedOptionIndex = selectedOptionIndex,
        hasAnswered = hasAnswered,
        onSelectOption = { index ->
            if (!hasAnswered) {
                selectedOptionIndex = index
            }
        },
        onSubmitAnswer = {
            if (selectedOptionIndex != null) {
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
            if (currentIndex < questions.size - 1) {
                currentIndex++
                selectedOptionIndex = null
                hasAnswered = false
            } else {
                val score = ((correctCount.toFloat() / questions.size.toFloat()) * 100).toInt()
                onTestFinished(
                    TestResult(
                        category = TestCategory.READING,
                        level = level,
                        score = score,
                        totalQuestions = questions.size,
                        correctCount = correctCount,
                        reviews = userReviews.toList()
                    )
                )
            }
        },
        onBackClick = onBackClick,
        modifier = modifier
    )
}

@Composable
fun ReadingTestContent(
    level: DifficultyLevel,
    currentIndex: Int,
    totalQuestions: Int,
    currentQuestion: ReadingQuestion,
    selectedOptionIndex: Int?,
    hasAnswered: Boolean,
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
            category = TestCategory.READING,
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
            // Passage Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderLight)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = PrimaryBlue.copy(alpha = 0.1f),
                            shape = CircleShape,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = "📖", fontSize = 16.sp)
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = currentQuestion.passageTitle,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = PrimaryBlueDark
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = currentQuestion.passageText,
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
                    )
                }
            }

            // Question Box
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
private fun getSampleReadingQuestions(level: DifficultyLevel): List<ReadingQuestion> = listOf(
    ReadingQuestion(
        id = "mock_r_1",
        level = level,
        passageTitle = "Daily Morning Routine",
        passageText = "Sarah wakes up at 6:00 AM every morning. She drinks a glass of warm lemon water and prepares a healthy breakfast before going for a brisk 30-minute jog in the neighborhood park.",
        questionText = "What does Sarah drink first in the morning?",
        options = listOf("Hot black coffee", "Warm lemon water", "Fresh orange juice", "Green tea with honey"),
        correctIndex = 1,
        explanation = "Paragraph 1 specifies that Sarah drinks a glass of warm lemon water right after waking up."
    )
)

// ==========================================
// PREVIEWS
// ==========================================

@Preview(showBackground = true, name = "Reading Test - Interactive Mode")
@Composable
private fun ReadingTestInteractivePreview() {
    AppMobileEnglishTrickTheme {
        ReadingTestScreen(
            level = DifficultyLevel.EASY,
            onTestFinished = {},
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Reading Test Content - Question State")
@Composable
private fun ReadingTestContentQuestionPreview() {
    AppMobileEnglishTrickTheme {
        ReadingTestContent(
            level = DifficultyLevel.EASY,
            currentIndex = 0,
            totalQuestions = 3,
            currentQuestion = getSampleReadingQuestions(DifficultyLevel.EASY)[0],
            selectedOptionIndex = 1,
            hasAnswered = false,
            onSelectOption = {},
            onSubmitAnswer = {},
            onNextQuestion = {},
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Reading Test Content - Answered State")
@Composable
private fun ReadingTestContentAnsweredPreview() {
    AppMobileEnglishTrickTheme {
        ReadingTestContent(
            level = DifficultyLevel.MEDIUM,
            currentIndex = 1,
            totalQuestions = 3,
            currentQuestion = getSampleReadingQuestions(DifficultyLevel.MEDIUM)[0],
            selectedOptionIndex = 1,
            hasAnswered = true,
            onSelectOption = {},
            onSubmitAnswer = {},
            onNextQuestion = {},
            onBackClick = {}
        )
    }
}

