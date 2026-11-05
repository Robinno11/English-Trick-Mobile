package com.example.appmobileenglishtrick.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.appmobileenglishtrick.ui.theme.AppMobileEnglishTrickTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appmobileenglishtrick.R
import com.example.appmobileenglishtrick.ui.theme.LogoCyan
import com.example.appmobileenglishtrick.ui.theme.LogoPink
import com.example.appmobileenglishtrick.data.model.DifficultyLevel
import com.example.appmobileenglishtrick.data.model.TestCategory
import com.example.appmobileenglishtrick.ui.theme.AccentAmber
import com.example.appmobileenglishtrick.ui.theme.AccentOrange
import com.example.appmobileenglishtrick.ui.theme.CardBorderLight
import com.example.appmobileenglishtrick.ui.theme.LevelEasy
import com.example.appmobileenglishtrick.ui.theme.LevelEasyBg
import com.example.appmobileenglishtrick.ui.theme.LevelHard
import com.example.appmobileenglishtrick.ui.theme.LevelHardBg
import com.example.appmobileenglishtrick.ui.theme.LevelMedium
import com.example.appmobileenglishtrick.ui.theme.LevelMediumBg
import com.example.appmobileenglishtrick.ui.theme.PrimaryBlue
import com.example.appmobileenglishtrick.ui.theme.PrimaryBlueDark
import com.example.appmobileenglishtrick.ui.theme.PrimaryBlueLight

@Composable
fun SloganBanner(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(PrimaryBlueDark, PrimaryBlue, PrimaryBlueLight)
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Surface(
                            color = Color.White,
                            shape = RoundedCornerShape(14.dp),
                            shadowElevation = 4.dp,
                            modifier = Modifier.size(62.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.logo_english_trick),
                                contentDescription = "Logo English Trick",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(4.dp)
                                    .clip(RoundedCornerShape(10.dp))
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(
                                text = "ENGLISH TRICK",
                                color = AccentAmber,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 20.sp,
                                letterSpacing = 1.2.sp
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "NO TRICK, NO SPEAK",
                                color = Color.White.copy(alpha = 0.9f),
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                letterSpacing = 0.8.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Slogan Highlight Badge
                Surface(
                    color = AccentOrange.copy(alpha = 0.25f),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AccentAmber.copy(alpha = 0.5f))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(text = "🔥", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "English Trick No Speak No Trick",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}


// Alignment helper
private fun Alignment.Companion.CenterVertornotCenter(): Alignment.Vertical = Alignment.CenterVertically

@Composable
fun CategoryCard(
    category: TestCategory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderLight)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = when (category) {
                    TestCategory.READING -> Color(0xFFEFF6FF)
                    TestCategory.LISTENING -> Color(0xFFFDF2F8)
                    TestCategory.SPEAKING -> Color(0xFFFFFBEB)
                    TestCategory.WRITING -> Color(0xFFF0FDF4)
                },
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.size(54.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = category.iconEmoji, fontSize = 26.sp)
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = category.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        color = PrimaryBlue.copy(alpha = 0.08f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "3 Levels",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = PrimaryBlue,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = category.subtitle,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                )
            }

            Text(text = "➔", fontSize = 18.sp, color = PrimaryBlueLight)
        }
    }
}

@Composable
fun LevelCard(
    level: DifficultyLevel,
    questionCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val levelColor = when (level) {
        DifficultyLevel.EASY -> LevelEasy
        DifficultyLevel.MEDIUM -> LevelMedium
        DifficultyLevel.HARD -> LevelHard
    }

    val levelBg = when (level) {
        DifficultyLevel.EASY -> LevelEasyBg
        DifficultyLevel.MEDIUM -> LevelMediumBg
        DifficultyLevel.HARD -> LevelHardBg
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, levelColor.copy(alpha = 0.35f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = levelBg,
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, levelColor.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = level.tag,
                        color = levelColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "⏱️ ~${level.estimatedMinutes}m",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "⭐ +${level.xpReward} XP",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentOrange
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "${level.title} Level Test",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = level.description,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📝 $questionCount Questions Available",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = PrimaryBlue
                )

                Surface(
                    color = levelColor,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Start Test ➔",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TestProgressHeader(
    category: TestCategory,
    level: DifficultyLevel,
    currentIndex: Int,
    totalQuestions: Int,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val levelColor = when (level) {
        DifficultyLevel.EASY -> LevelEasy
        DifficultyLevel.MEDIUM -> LevelMedium
        DifficultyLevel.HARD -> LevelHard
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onBackClick() }
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                    shape = CircleShape,
                    modifier = Modifier.size(34.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "✕", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "${category.iconEmoji} ${category.title}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Surface(
                color = levelColor.copy(alpha = 0.15f),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, levelColor.copy(alpha = 0.5f))
            ) {
                Text(
                    text = level.title,
                    color = levelColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Question ${currentIndex + 1} of $totalQuestions",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            val progressPercent = ((currentIndex + 1).toFloat() / totalQuestions.toFloat() * 100).toInt()
            Text(
                text = "$progressPercent%",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        LinearProgressIndicator(
            progress = { (currentIndex + 1).toFloat() / totalQuestions.toFloat() },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = PrimaryBlueLight,
            trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
        )
    }
}

@Composable
fun PulsingWaveIndicator(
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    if (!isPlaying) return

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale1 by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale1"
    )
    val scale2 by infiniteTransition.animateFloat(
        initialValue = 1.2f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale2"
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .scale(scale1)
                .background(AccentOrange, CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Box(
            modifier = Modifier
                .size(14.dp)
                .scale(scale2)
                .background(PrimaryBlueLight, CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Box(
            modifier = Modifier
                .size(10.dp)
                .scale(scale1)
                .background(AccentAmber, CircleShape)
        )
    }
}

@Preview(showBackground = true, name = "Slogan Banner Preview")
@Composable
private fun SloganBannerPreview() {
    AppMobileEnglishTrickTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            SloganBanner()
        }
    }
}

@Preview(showBackground = true, name = "Category Card Preview")
@Composable
private fun CategoryCardPreview() {
    AppMobileEnglishTrickTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            CategoryCard(
                category = TestCategory.SPEAKING,
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Level Card Preview")
@Composable
private fun LevelCardPreview() {
    AppMobileEnglishTrickTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            LevelCard(
                level = DifficultyLevel.MEDIUM,
                questionCount = 10,
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Test Progress Header Preview")
@Composable
private fun TestProgressHeaderPreview() {
    AppMobileEnglishTrickTheme {
        TestProgressHeader(
            category = TestCategory.LISTENING,
            level = DifficultyLevel.HARD,
            currentIndex = 2,
            totalQuestions = 5,
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Pulsing Wave Indicator Preview")
@Composable
private fun PulsingWaveIndicatorPreview() {
    AppMobileEnglishTrickTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            PulsingWaveIndicator(isPlaying = true)
        }
    }
}

