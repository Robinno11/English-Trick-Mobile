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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.appmobileenglishtrick.ui.theme.AppMobileEnglishTrickTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appmobileenglishtrick.data.model.TestCategory
import com.example.appmobileenglishtrick.ui.components.CategoryCard
import com.example.appmobileenglishtrick.ui.components.SloganBanner
import com.example.appmobileenglishtrick.ui.theme.AccentAmber
import com.example.appmobileenglishtrick.ui.theme.AccentOrange
import com.example.appmobileenglishtrick.ui.theme.BackgroundLight
import com.example.appmobileenglishtrick.ui.theme.CardBorderLight
import com.example.appmobileenglishtrick.ui.theme.LevelEasy
import com.example.appmobileenglishtrick.ui.theme.PrimaryBlue
import com.example.appmobileenglishtrick.ui.theme.PrimaryBlueDark

@Composable
fun HomeScreen(
    onSelectCategory: (TestCategory) -> Unit,
    completedTestsCount: Int,
    averageScore: Int,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // 1. Top Brand Slogan Banner
        item {
            SloganBanner()
        }

        // 2. Learning Stats Overview
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
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatItem(
                        icon = "🔥",
                        value = "3 Days",
                        label = "Streak"
                    )
                    Box(
                        modifier = Modifier
                            .height(36.dp)
                            .width(1.dp)
                            .background(CardBorderLight)
                    )
                    StatItem(
                        icon = "📊",
                        value = "$completedTestsCount",
                        label = "Tests Taken"
                    )
                    Box(
                        modifier = Modifier
                            .height(36.dp)
                            .width(1.dp)
                            .background(CardBorderLight)
                    )
                    StatItem(
                        icon = "⭐",
                        value = if (completedTestsCount > 0) "$averageScore%" else "--",
                        label = "Avg Score"
                    )
                }
            }
        }

        // 3. Section Title: Choose Test Menu
        item {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Core Test Modules",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Surface(
                        color = AccentAmber.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "4 Categories",
                            color = AccentOrange,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Select any skill category to begin testing your English proficiency.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f)
                )
            }
        }

        // 4. The 4 Categories Cards
        item {
            CategoryCard(
                category = TestCategory.READING,
                onClick = { onSelectCategory(TestCategory.READING) }
            )
        }

        item {
            CategoryCard(
                category = TestCategory.LISTENING,
                onClick = { onSelectCategory(TestCategory.LISTENING) }
            )
        }

        item {
            CategoryCard(
                category = TestCategory.SPEAKING,
                onClick = { onSelectCategory(TestCategory.SPEAKING) }
            )
        }

        item {
            CategoryCard(
                category = TestCategory.WRITING,
                onClick = { onSelectCategory(TestCategory.WRITING) }
            )
        }

        // 5. English Trick Philosophy Footer
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = PrimaryBlue.copy(alpha = 0.05f)
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "💡", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "English Trick Tip",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = PrimaryBlue
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "True fluency comes from regular vocal training. English Trick No Speak No Trick!",
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun StatItem(
    icon: String,
    value: String,
    label: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = icon, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = value,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Preview(showBackground = true, name = "Home Screen - With Stats")
@Composable
private fun HomeScreenWithStatsPreview() {
    AppMobileEnglishTrickTheme {
        HomeScreen(
            onSelectCategory = {},
            completedTestsCount = 12,
            averageScore = 85
        )
    }
}

@Preview(showBackground = true, name = "Home Screen - Empty Stats")
@Composable
private fun HomeScreenEmptyPreview() {
    AppMobileEnglishTrickTheme {
        HomeScreen(
            onSelectCategory = {},
            completedTestsCount = 0,
            averageScore = 0
        )
    }
}

@Preview(showBackground = true, name = "Home Screen - Interactive")
@Composable
private fun HomeScreenInteractivePreview() {
    AppMobileEnglishTrickTheme {
        var testCount by remember { mutableIntStateOf(3) }
        var avgScore by remember { mutableIntStateOf(78) }

        HomeScreen(
            onSelectCategory = {
                testCount++
                avgScore = (avgScore + 85) / 2
            },
            completedTestsCount = testCount,
            averageScore = avgScore
        )
    }
}

