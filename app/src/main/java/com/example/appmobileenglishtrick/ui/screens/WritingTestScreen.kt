package com.example.appmobileenglishtrick.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.appmobileenglishtrick.data.model.TestCategory
import com.example.appmobileenglishtrick.data.model.TestResult
import com.example.appmobileenglishtrick.data.model.WritingQuestion
import com.example.appmobileenglishtrick.data.repository.TestRepository
import com.example.appmobileenglishtrick.ui.components.TestProgressHeader
import com.example.appmobileenglishtrick.ui.theme.AccentAmber
import com.example.appmobileenglishtrick.ui.theme.AccentOrange
import com.example.appmobileenglishtrick.ui.theme.CardBorderLight
import com.example.appmobileenglishtrick.ui.theme.LevelEasy
import com.example.appmobileenglishtrick.ui.theme.LevelHard
import com.example.appmobileenglishtrick.ui.theme.LevelMedium
import com.example.appmobileenglishtrick.ui.theme.PrimaryBlue
import com.example.appmobileenglishtrick.ui.theme.PrimaryBlueDark

@Composable
fun WritingTestScreen(
    level: DifficultyLevel,
    onTestFinished: (TestResult) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val questions = remember(level) {
        val loaded = TestRepository.getQuestions(TestCategory.WRITING, level).filterIsInstance<WritingQuestion>()
        if (loaded.isNotEmpty()) loaded else getSampleWritingQuestions(level)
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    var userText by remember { mutableStateOf("") }
    var hasEvaluated by remember { mutableStateOf(false) }

    val userReviews = remember { mutableStateListOf<QuestionReview>() }
    var totalScoreSum by remember { mutableIntStateOf(0) }

    if (questions.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "No questions available.")
        }
        return
    }

    val currentQuestion = questions[currentIndex]

    WritingTestContent(
        level = level,
        currentIndex = currentIndex,
        totalQuestions = questions.size,
        currentQuestion = currentQuestion,
        userText = userText,
        hasEvaluated = hasEvaluated,
        onTextChange = { newText ->
            if (!hasEvaluated) {
                userText = newText
            }
        },
        onSubmitEvaluation = {
            hasEvaluated = true
            val words = userText.trim().split("\\s+".toRegex()).filter { it.isNotBlank() }
            val wordCount = words.size
            val usedKeywordsCount = currentQuestion.suggestedKeywords.count {
                userText.contains(it, ignoreCase = true)
            }
            val lengthScore = ((wordCount.toFloat() / currentQuestion.minWords.toFloat()) * 50).coerceAtMost(50f)
            val keywordScore = if (currentQuestion.suggestedKeywords.isNotEmpty()) {
                (usedKeywordsCount.toFloat() / currentQuestion.suggestedKeywords.size.toFloat()) * 50f
            } else 50f
            val questionScore = (lengthScore + keywordScore).toInt().coerceIn(0, 100)
            totalScoreSum += questionScore

            userReviews.add(
                QuestionReview(
                    questionNumber = currentIndex + 1,
                    prompt = currentQuestion.topicTitle,
                    userAnswer = "Words: $wordCount, Keywords: $usedKeywordsCount/${currentQuestion.suggestedKeywords.size}",
                    correctAnswer = "Min ${currentQuestion.minWords} words",
                    isCorrect = questionScore >= 60,
                    explanation = "Score: $questionScore/100. ${currentQuestion.grammarTips}"
                )
            )
        },
        onNextQuestion = {
            if (currentIndex < questions.size - 1) {
                currentIndex++
                userText = ""
                hasEvaluated = false
            } else {
                val avgScore = (totalScoreSum.toFloat() / questions.size.toFloat()).toInt()
                val correctCount = userReviews.count { it.isCorrect }
                onTestFinished(
                    TestResult(
                        category = TestCategory.WRITING,
                        level = level,
                        score = avgScore,
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WritingTestContent(
    level: DifficultyLevel,
    currentIndex: Int,
    totalQuestions: Int,
    currentQuestion: WritingQuestion,
    userText: String,
    hasEvaluated: Boolean,
    onTextChange: (String) -> Unit,
    onSubmitEvaluation: () -> Unit,
    onNextQuestion: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    // Real-time word count calculation
    val words = remember(userText) {
        userText.trim().split("\\s+".toRegex()).filter { it.isNotBlank() }
    }
    val wordCount = words.size
    val isWordCountMet = wordCount >= currentQuestion.minWords

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Progress Header
        TestProgressHeader(
            category = TestCategory.WRITING,
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
            // Topic Prompt Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderLight)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = currentQuestion.topicTitle,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = PrimaryBlueDark
                        )

                        Surface(
                            color = PrimaryBlue.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "Min ${currentQuestion.minWords} words",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = PrimaryBlue,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = currentQuestion.prompt,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Suggested Keywords Tags
                    Text(
                        text = "Suggested Keywords:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        currentQuestion.suggestedKeywords.forEach { keyword ->
                            val isUsed = userText.contains(keyword, ignoreCase = true)
                            Surface(
                                color = if (isUsed) LevelEasy.copy(alpha = 0.15f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                                shape = RoundedCornerShape(6.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isUsed) LevelEasy else CardBorderLight
                                )
                            ) {
                                Text(
                                    text = if (isUsed) "✓ $keyword" else "+ $keyword",
                                    fontSize = 11.sp,
                                    fontWeight = if (isUsed) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isUsed) LevelEasy else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Text Input Editor Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, if (isWordCountMet) LevelEasy else CardBorderLight)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = userText,
                        onValueChange = onTextChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        placeholder = {
                            Text(
                                text = "Start typing your English response here...\nRemember: English Trick No Speak No Trick!",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Word count indicator
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${userText.length} characters",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )

                        Surface(
                            color = if (isWordCountMet) LevelEasy.copy(alpha = 0.15f) else LevelHard.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "$wordCount / ${currentQuestion.minWords} words",
                                color = if (isWordCountMet) LevelEasy else LevelHard,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // Post-Evaluation Feedback & Model Answer
            if (hasEvaluated) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, LevelEasy.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🌟", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "English Trick Model Answer",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = PrimaryBlue
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = currentQuestion.modelAnswer,
                            fontSize = 13.sp,
                            lineHeight = 20.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Surface(
                            color = AccentAmber.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "💡 Writing Tips:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = AccentOrange
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = currentQuestion.grammarTips,
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
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
                if (!hasEvaluated) {
                    Button(
                        onClick = onSubmitEvaluation,
                        enabled = wordCount >= (currentQuestion.minWords / 2),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                    ) {
                        Text(text = "Submit & Review Essay", fontWeight = FontWeight.Bold, fontSize = 15.sp)
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
                            text = if (currentIndex < totalQuestions - 1) "Next Writing Prompt ➔" else "See Writing Results 🏆",
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
private fun getSampleWritingQuestions(level: DifficultyLevel): List<WritingQuestion> = listOf(
    WritingQuestion(
        id = "mock_w_1",
        level = level,
        topicTitle = "Future Career Goals",
        prompt = "Describe your ideal job in the next 5 years and why English communication skills are essential for this role.",
        minWords = 25,
        suggestedKeywords = listOf("career", "opportunity", "communication", "skills", "global"),
        modelAnswer = "In the next five years, my career goal is to work as a software engineer at a global technology company. Strong English communication skills are essential to collaborate with international teams and share project updates effectively.",
        grammarTips = "Use future tense ('will', 'aim to') and conditional structures ('in order to', 'so that') to outline your ambitions clearly."
    )
)

// ==========================================
// PREVIEWS
// ==========================================

@Preview(showBackground = true, name = "Writing Test - Interactive Mode")
@Composable
private fun WritingTestInteractivePreview() {
    AppMobileEnglishTrickTheme {
        WritingTestScreen(
            level = DifficultyLevel.EASY,
            onTestFinished = {},
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Writing Test Content - Editing State")
@Composable
private fun WritingTestContentEditingPreview() {
    AppMobileEnglishTrickTheme {
        WritingTestContent(
            level = DifficultyLevel.EASY,
            currentIndex = 0,
            totalQuestions = 3,
            currentQuestion = getSampleWritingQuestions(DifficultyLevel.EASY)[0],
            userText = "In the future, my career ambition is to become a software architect. Good communication skills are necessary.",
            hasEvaluated = false,
            onTextChange = {},
            onSubmitEvaluation = {},
            onNextQuestion = {},
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Writing Test Content - Evaluated State")
@Composable
private fun WritingTestContentEvaluatedPreview() {
    AppMobileEnglishTrickTheme {
        WritingTestContent(
            level = DifficultyLevel.MEDIUM,
            currentIndex = 1,
            totalQuestions = 3,
            currentQuestion = getSampleWritingQuestions(DifficultyLevel.MEDIUM)[0],
            userText = "In the future, my career ambition is to become a software architect. Good communication skills are necessary to connect with global developers.",
            hasEvaluated = true,
            onTextChange = {},
            onSubmitEvaluation = {},
            onNextQuestion = {},
            onBackClick = {}
        )
    }
}

