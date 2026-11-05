package com.example.appmobileenglishtrick.data.model

sealed interface BaseQuestion {
    val id: String
    val category: TestCategory
    val level: DifficultyLevel
}

data class ReadingQuestion(
    override val id: String,
    override val level: DifficultyLevel,
    val passageTitle: String,
    val passageText: String,
    val questionText: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
) : BaseQuestion {
    override val category: TestCategory = TestCategory.READING
}

data class ListeningQuestion(
    override val id: String,
    override val level: DifficultyLevel,
    val audioTitle: String,
    val audioSpeaker: String,
    val audioScript: String,
    val questionText: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
) : BaseQuestion {
    override val category: TestCategory = TestCategory.LISTENING
}

data class SpeakingQuestion(
    override val id: String,
    override val level: DifficultyLevel,
    val title: String,
    val context: String,
    val targetSentence: String,
    val phoneticGuide: String,
    val tip: String
) : BaseQuestion {
    override val category: TestCategory = TestCategory.SPEAKING
}

data class WritingQuestion(
    override val id: String,
    override val level: DifficultyLevel,
    val topicTitle: String,
    val prompt: String,
    val minWords: Int,
    val suggestedKeywords: List<String>,
    val modelAnswer: String,
    val grammarTips: String
) : BaseQuestion {
    override val category: TestCategory = TestCategory.WRITING
}
