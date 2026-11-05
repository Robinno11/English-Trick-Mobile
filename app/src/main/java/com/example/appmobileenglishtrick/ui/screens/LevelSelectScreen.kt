package com.example.appmobileenglishtrick.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.appmobileenglishtrick.ui.theme.AppMobileEnglishTrickTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appmobileenglishtrick.R
import com.example.appmobileenglishtrick.data.model.DifficultyLevel
import com.example.appmobileenglishtrick.data.model.TestCategory
import com.example.appmobileenglishtrick.data.repository.TestRepository
import com.example.appmobileenglishtrick.ui.components.LevelCard
import com.example.appmobileenglishtrick.ui.theme.AccentOrange
import com.example.appmobileenglishtrick.ui.theme.CardBorderLight
import com.example.appmobileenglishtrick.ui.theme.PrimaryBlue
import com.example.appmobileenglishtrick.ui.theme.PrimaryBlueLight

@Composable
fun LevelSelectScreen(
    category: TestCategory,
    onSelectLevel: (DifficultyLevel) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Navigation Bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = MaterialTheme.colorScheme.surface,
                        shape = CircleShape,
                        modifier = Modifier
                            .size(42.dp)
                            .clickable { onBackClick() },
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderLight)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = "←", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "${category.iconEmoji} ${category.title} Test",
                            fontWeight = FontWeight.Bold,
                            fontSize = 19.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Choose Your Challenge Level",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f)
                        )
                    }
                }

                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(10.dp),
                    shadowElevation = 2.dp,
                    modifier = Modifier.size(42.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_english_trick),
                        contentDescription = "Logo English Trick",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(2.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
            }
        }


        // Category Detail Description Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderLight)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "📌", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Assessment Focus",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = PrimaryBlue
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = category.description,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                }
            }
        }

        // Levels: Easy, Medium, Hard
        item {
            LevelCard(
                level = DifficultyLevel.EASY,
                questionCount = TestRepository.getTotalQuestionCount(category, DifficultyLevel.EASY),
                onClick = { onSelectLevel(DifficultyLevel.EASY) }
            )
        }

        item {
            LevelCard(
                level = DifficultyLevel.MEDIUM,
                questionCount = TestRepository.getTotalQuestionCount(category, DifficultyLevel.MEDIUM),
                onClick = { onSelectLevel(DifficultyLevel.MEDIUM) }
            )
        }

        item {
            LevelCard(
                level = DifficultyLevel.HARD,
                questionCount = TestRepository.getTotalQuestionCount(category, DifficultyLevel.HARD),
                onClick = { onSelectLevel(DifficultyLevel.HARD) }
            )
        }

        // English Trick Motto Footer
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "“English Trick No Speak No Trick”",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = PrimaryBlue.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Level Select - Speaking")
@Composable
private fun LevelSelectSpeakingPreview() {
    AppMobileEnglishTrickTheme {
        LevelSelectScreen(
            category = TestCategory.SPEAKING,
            onSelectLevel = {},
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Level Select - Reading")
@Composable
private fun LevelSelectReadingPreview() {
    AppMobileEnglishTrickTheme {
        LevelSelectScreen(
            category = TestCategory.READING,
            onSelectLevel = {},
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Level Select - Interactive")
@Composable
private fun LevelSelectInteractivePreview() {
    AppMobileEnglishTrickTheme {
        var selectedCategory by remember { mutableStateOf(TestCategory.SPEAKING) }
        var selectedLevel by remember { mutableStateOf<DifficultyLevel?>(null) }

        Column {
            LevelSelectScreen(
                category = selectedCategory,
                onSelectLevel = { level ->
                    selectedLevel = level
                },
                onBackClick = {
                    selectedCategory = if (selectedCategory == TestCategory.SPEAKING) {
                        TestCategory.LISTENING
                    } else {
                        TestCategory.SPEAKING
                    }
                }
            )
        }
    }
}

