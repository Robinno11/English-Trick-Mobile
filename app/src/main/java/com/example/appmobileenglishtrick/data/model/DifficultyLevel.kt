package com.example.appmobileenglishtrick.data.model

enum class DifficultyLevel(
    val id: String,
    val title: String,
    val tag: String,
    val description: String,
    val estimatedMinutes: Int,
    val xpReward: Int
) {
    EASY(
        id = "easy",
        title = "Easy",
        tag = "Level 1 • Beginner",
        description = "Basic vocabulary, simple daily dialogues, and fundamental sentence structure.",
        estimatedMinutes = 5,
        xpReward = 50
    ),
    MEDIUM(
        id = "medium",
        title = "Medium",
        tag = "Level 2 • Intermediate",
        description = "Conversational fluency, practical workplace scenarios, and complex grammar.",
        estimatedMinutes = 10,
        xpReward = 100
    ),
    HARD(
        id = "hard",
        title = "Hard",
        tag = "Level 3 • Advanced",
        description = "Academic passages, native idioms, advanced reasoning, and professional discourse.",
        estimatedMinutes = 15,
        xpReward = 150
    )
}
