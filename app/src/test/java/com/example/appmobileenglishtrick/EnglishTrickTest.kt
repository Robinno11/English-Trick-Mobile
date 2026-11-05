package com.example.appmobileenglishtrick

import com.example.appmobileenglishtrick.audio.SpeechInputManager
import com.example.appmobileenglishtrick.data.model.DifficultyLevel
import com.example.appmobileenglishtrick.data.model.TestCategory
import com.example.appmobileenglishtrick.data.model.TestResult
import com.example.appmobileenglishtrick.data.repository.TestRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class EnglishTrickTest {

    @Test
    fun testAllCategoriesAndLevelsHaveQuestions() {
        for (category in TestCategory.entries) {
            for (level in DifficultyLevel.entries) {
                val questions = TestRepository.getQuestions(category, level)
                assertTrue(
                    "Category ${category.title} Level ${level.title} should have at least 1 question",
                    questions.isNotEmpty()
                )
            }
        }
    }

    @Test
    fun testSpeechSimilarityScoring() {
        val target = "English Trick No Speak No Trick"
        
        // Exact match
        val scoreExact = SpeechInputManager.calculateAccuracy(target, "English Trick No Speak No Trick")
        assertEquals(100, scoreExact)

        // Case insensitivity
        val scoreCase = SpeechInputManager.calculateAccuracy(target, "english trick no speak no trick")
        assertEquals(100, scoreCase)

        // Close match with punctuation
        val scoreClose = SpeechInputManager.calculateAccuracy(target, "English Trick, no speak no trick!")
        assertTrue("Score should be >= 90 for identical words with punctuation", scoreClose >= 90)

        // Completely unrelated
        val scoreIrrelevant = SpeechInputManager.calculateAccuracy(target, "The quick brown fox jumps")
        assertTrue("Score should be <= 25 for irrelevant phrase", scoreIrrelevant <= 25)
    }

    @Test
    fun testResultFeedbackIncludesSlogan() {
        val resultA = TestResult(
            category = TestCategory.SPEAKING,
            level = DifficultyLevel.EASY,
            score = 90,
            totalQuestions = 3,
            correctCount = 3,
            reviews = emptyList()
        )
        assertEquals("A (Excellent)", resultA.grade)
        assertTrue(resultA.feedbackMessage.contains("No Speak No Trick"))

        val resultC = TestResult(
            category = TestCategory.READING,
            level = DifficultyLevel.MEDIUM,
            score = 60,
            totalQuestions = 3,
            correctCount = 2,
            reviews = emptyList()
        )
        assertEquals("C (Needs Practice)", resultC.grade)
        assertTrue(resultC.feedbackMessage.contains("No Speak No Trick"))
    }
}
