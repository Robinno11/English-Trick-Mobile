package com.example.appmobileenglishtrick.data.model

data class QuestionReview(
    val questionNumber: Int,
    val prompt: String,
    val userAnswer: String,
    val correctAnswer: String,
    val isCorrect: Boolean,
    val explanation: String
)

data class TestResult(
    val category: TestCategory,
    val level: DifficultyLevel,
    val score: Int, // 0 - 100
    val totalQuestions: Int,
    val correctCount: Int,
    val reviews: List<QuestionReview>,
    val timeSpentSeconds: Int = 0
) {
    val grade: String
        get() = when {
            score >= 85 -> "A (Excellent)"
            score >= 70 -> "B (Good Job)"
            score >= 55 -> "C (Needs Practice)"
            else -> "D (Keep Trying)"
        }

    val feedbackMessage: String
        get() = when {
            score >= 85 -> "Outstanding! You proved: English Trick No Speak No Trick!"
            score >= 70 -> "Great performance! Keep sharpening your skills with English Trick!"
            score >= 55 -> "Good effort! Remember: No Speak No Trick, practice consistently!"
            else -> "Don't give up! Practice makes perfect with English Trick!"
        }
}
