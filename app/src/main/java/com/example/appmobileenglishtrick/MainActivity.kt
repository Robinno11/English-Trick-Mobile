package com.example.appmobileenglishtrick

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.appmobileenglishtrick.audio.EnglishTtsManager
import com.example.appmobileenglishtrick.data.model.DifficultyLevel
import com.example.appmobileenglishtrick.data.model.QuestionReview
import com.example.appmobileenglishtrick.data.model.TestCategory
import com.example.appmobileenglishtrick.data.model.TestResult
import com.example.appmobileenglishtrick.ui.screens.HomeScreen
import com.example.appmobileenglishtrick.ui.screens.LevelSelectScreen
import com.example.appmobileenglishtrick.ui.screens.ListeningTestScreen
import com.example.appmobileenglishtrick.ui.screens.ReadingTestScreen
import com.example.appmobileenglishtrick.ui.screens.ResultScreen
import com.example.appmobileenglishtrick.ui.screens.SpeakingTestScreen
import com.example.appmobileenglishtrick.ui.screens.WritingTestScreen
import com.example.appmobileenglishtrick.ui.theme.AppMobileEnglishTrickTheme

sealed interface AppScreen {
    data object Home : AppScreen
    data class LevelSelect(val category: TestCategory) : AppScreen
    data class Test(val category: TestCategory, val level: DifficultyLevel) : AppScreen
    data class Result(val result: TestResult) : AppScreen
}

class MainActivity : ComponentActivity() {

    private var ttsManager: EnglishTtsManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        ttsManager = EnglishTtsManager(this)

        setContent {
            AppMobileEnglishTrickTheme {
                MainApp(ttsManager = ttsManager)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ttsManager?.shutdown()
    }
}

@Composable
fun SafeBackHandler(enabled: Boolean = true, onBack: () -> Unit) {
    if (LocalOnBackPressedDispatcherOwner.current != null) {
        BackHandler(enabled = enabled, onBack = onBack)
    }
}

@Composable
fun MainApp(
    ttsManager: EnglishTtsManager? = null,
    initialScreen: AppScreen = AppScreen.Home
) {
    var currentScreen by remember { mutableStateOf<AppScreen>(initialScreen) }

    // User statistics
    var completedTestsCount by remember { mutableIntStateOf(0) }
    var totalScoreAccumulated by remember { mutableIntStateOf(0) }

    val averageScore = if (completedTestsCount > 0) {
        totalScoreAccumulated / completedTestsCount
    } else 0

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        when (val screen = currentScreen) {
            is AppScreen.Home -> {
                HomeScreen(
                    onSelectCategory = { category ->
                        currentScreen = AppScreen.LevelSelect(category)
                    },
                    completedTestsCount = completedTestsCount,
                    averageScore = averageScore,
                    modifier = Modifier.padding(innerPadding)
                )
            }

            is AppScreen.LevelSelect -> {
                SafeBackHandler {
                    currentScreen = AppScreen.Home
                }
                LevelSelectScreen(
                    category = screen.category,
                    onSelectLevel = { level ->
                        currentScreen = AppScreen.Test(screen.category, level)
                    },
                    onBackClick = {
                        currentScreen = AppScreen.Home
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            is AppScreen.Test -> {
                SafeBackHandler {
                    ttsManager?.stop()
                    currentScreen = AppScreen.LevelSelect(screen.category)
                }

                val onTestFinished: (TestResult) -> Unit = { result ->
                    completedTestsCount++
                    totalScoreAccumulated += result.score
                    currentScreen = AppScreen.Result(result)
                }

                when (screen.category) {
                    TestCategory.READING -> {
                        ReadingTestScreen(
                            level = screen.level,
                            onTestFinished = onTestFinished,
                            onBackClick = {
                                currentScreen = AppScreen.LevelSelect(screen.category)
                            },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }

                    TestCategory.LISTENING -> {
                        ListeningTestScreen(
                            level = screen.level,
                            ttsManager = ttsManager,
                            onTestFinished = onTestFinished,
                            onBackClick = {
                                ttsManager?.stop()
                                currentScreen = AppScreen.LevelSelect(screen.category)
                            },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }

                    TestCategory.SPEAKING -> {
                        SpeakingTestScreen(
                            level = screen.level,
                            ttsManager = ttsManager,
                            onTestFinished = onTestFinished,
                            onBackClick = {
                                ttsManager?.stop()
                                currentScreen = AppScreen.LevelSelect(screen.category)
                            },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }

                    TestCategory.WRITING -> {
                        WritingTestScreen(
                            level = screen.level,
                            onTestFinished = onTestFinished,
                            onBackClick = {
                                currentScreen = AppScreen.LevelSelect(screen.category)
                            },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }

            is AppScreen.Result -> {
                SafeBackHandler {
                    currentScreen = AppScreen.Home
                }
                ResultScreen(
                    result = screen.result,
                    onRetakeTest = {
                        currentScreen = AppScreen.Test(screen.result.category, screen.result.level)
                    },
                    onBackHome = {
                        currentScreen = AppScreen.Home
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Previews
// ---------------------------------------------------------------------------

@Preview(showBackground = true, name = "Main App - Interactive Flow")
@Composable
private fun MainAppInteractivePreview() {
    AppMobileEnglishTrickTheme {
        MainApp(
            ttsManager = null,
            initialScreen = AppScreen.Home
        )
    }
}

@Preview(showBackground = true, name = "Main App - Level Select Screen")
@Composable
private fun MainAppLevelSelectPreview() {
    AppMobileEnglishTrickTheme {
        MainApp(
            ttsManager = null,
            initialScreen = AppScreen.LevelSelect(TestCategory.LISTENING)
        )
    }
}

@Preview(showBackground = true, name = "Main App - Result Screen")
@Composable
private fun MainAppResultPreview() {
    AppMobileEnglishTrickTheme {
        MainApp(
            ttsManager = null,
            initialScreen = AppScreen.Result(
                TestResult(
                    category = TestCategory.READING,
                    level = DifficultyLevel.EASY,
                    score = 90,
                    totalQuestions = 10,
                    correctCount = 9,
                    reviews = listOf(
                        QuestionReview(
                            questionNumber = 1,
                            prompt = "Which word is a synonym for 'rapid'?",
                            userAnswer = "Fast",
                            correctAnswer = "Fast",
                            isCorrect = true,
                            explanation = "'Rapid' means happening in a short time or at great speed."
                        )
                    )
                )
            )
        )
    }
}