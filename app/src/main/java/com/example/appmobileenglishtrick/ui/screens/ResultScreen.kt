package com.example.appmobileenglishtrick.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.appmobileenglishtrick.data.model.QuestionReview
import com.example.appmobileenglishtrick.ui.theme.AppMobileEnglishTrickTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import com.example.appmobileenglishtrick.R
import com.example.appmobileenglishtrick.data.model.DifficultyLevel
import com.example.appmobileenglishtrick.data.model.TestCategory
import com.example.appmobileenglishtrick.data.model.TestResult
import com.example.appmobileenglishtrick.ui.theme.AccentAmber
import com.example.appmobileenglishtrick.ui.theme.AccentOrange
import com.example.appmobileenglishtrick.ui.theme.CardBorderLight
import com.example.appmobileenglishtrick.ui.theme.LevelEasy
import com.example.appmobileenglishtrick.ui.theme.LevelHard
import com.example.appmobileenglishtrick.ui.theme.LevelMedium
import com.example.appmobileenglishtrick.ui.theme.PrimaryBlue
import com.example.appmobileenglishtrick.ui.theme.PrimaryBlueDark
import com.example.appmobileenglishtrick.ui.theme.PrimaryBlueLight

@Composable
fun ResultScreen(
    result: TestResult,
    onRetakeTest: () -> Unit,
    onBackHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val levelColor = when (result.level) {
        DifficultyLevel.EASY -> LevelEasy
        DifficultyLevel.MEDIUM -> LevelMedium
        DifficultyLevel.HARD -> LevelHard
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Hero Score Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(PrimaryBlueDark, PrimaryBlue)
                            )
                        )
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Official English Trick Logo
                        Surface(
                            color = Color.White,
                            shape = RoundedCornerShape(14.dp),
                            shadowElevation = 4.dp,
                            modifier = Modifier.size(54.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.logo_english_trick),
                                contentDescription = "Logo English Trick",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(3.dp)
                                    .clip(RoundedCornerShape(11.dp))
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Category & Level Chips
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Surface(
                                color = Color.White.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "${result.category.iconEmoji} ${result.category.title}",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                color = levelColor,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = result.level.title,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Circular Score Badge
                        Surface(
                            color = Color.White.copy(alpha = 0.1f),
                            shape = CircleShape,
                            modifier = Modifier.size(110.dp),
                            border = androidx.compose.foundation.BorderStroke(3.dp, AccentAmber)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "${result.score}%",
                                        color = AccentAmber,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 28.sp
                                    )
                                    Text(
                                        text = result.grade,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = result.feedbackMessage,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // English Trick Slogan Banner inside score card
                        Surface(
                            color = AccentOrange.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, AccentAmber.copy(alpha = 0.5f))
                        ) {
                            Text(
                                text = "English Trick No Speak No Trick",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        // 2. Score Summary Stats Row
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderLight)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Total Questions", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "${result.totalQuestions}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Correct / Met", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "${result.correctCount}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = LevelEasy)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "XP Earned", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "+${result.level.xpReward}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = AccentOrange)
                    }
                }
            }
        }

        // 3. Question Reviews Title
        item {
            Text(
                text = "Question Review & Answers",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // 4. List of Question Reviews
        items(result.reviews) { review ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (review.isCorrect) LevelEasy.copy(alpha = 0.5f) else LevelHard.copy(alpha = 0.5f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Question ${review.questionNumber}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = PrimaryBlue
                        )

                        Surface(
                            color = if (review.isCorrect) LevelEasy.copy(alpha = 0.15f) else LevelHard.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = if (review.isCorrect) "✓ Correct" else "✕ Needs Work",
                                color = if (review.isCorrect) LevelEasy else LevelHard,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = review.prompt,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Your response: ${review.userAnswer}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Correct / Target: ${review.correctAnswer}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = LevelEasy
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Explanation: ${review.explanation}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }

        // 5. Action Buttons
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onRetakeTest,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Text(text = "Retake Test ↺", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = onBackHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, PrimaryBlue)
            ) {
                Text(text = "Back to Home 🏠", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = PrimaryBlue)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true, name = "Result Screen - High Score (A)")
@Composable
private fun ResultScreenHighScorePreview() {
    AppMobileEnglishTrickTheme {
        ResultScreen(
            result = TestResult(
                category = TestCategory.SPEAKING,
                level = DifficultyLevel.MEDIUM,
                score = 88,
                totalQuestions = 4,
                correctCount = 4,
                reviews = listOf(
                    QuestionReview(
                        questionNumber = 1,
                        prompt = "Could you please tell me how to get to the station?",
                        userAnswer = "Could you please tell me how to get to the station?",
                        correctAnswer = "Could you please tell me how to get to the station?",
                        isCorrect = true,
                        explanation = "Akurasi: 95%. Pelafalan intonasi sopan sangat natural."
                    ),
                    QuestionReview(
                        questionNumber = 2,
                        prompt = "I would appreciate your assistance with this project.",
                        userAnswer = "I would appreciate your assistance with this project.",
                        correctAnswer = "I would appreciate your assistance with this project.",
                        isCorrect = true,
                        explanation = "Akurasi: 90%. Penekanan kata 'appreciate' tepat."
                    )
                )
            ),
            onRetakeTest = {},
            onBackHome = {}
        )
    }
}

@Preview(showBackground = true, name = "Result Screen - Needs Practice (D)")
@Composable
private fun ResultScreenNeedsPracticePreview() {
    AppMobileEnglishTrickTheme {
        ResultScreen(
            result = TestResult(
                category = TestCategory.LISTENING,
                level = DifficultyLevel.HARD,
                score = 45,
                totalQuestions = 4,
                correctCount = 1,
                reviews = listOf(
                    QuestionReview(
                        questionNumber = 1,
                        prompt = "What was the main objection raised by the board?",
                        userAnswer = "They approved the timeline immediately",
                        correctAnswer = "The project budget exceeded the forecast by 25%",
                        isCorrect = false,
                        explanation = "Pembicara menyatakan bahwa biaya melebihi anggaran yang disetujui."
                    )
                )
            ),
            onRetakeTest = {},
            onBackHome = {}
        )
    }
}

