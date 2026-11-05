package com.example.appmobileenglishtrick.data.model

enum class TestCategory(
    val id: String,
    val title: String,
    val subtitle: String,
    val iconEmoji: String,
    val description: String
) {
    READING(
        id = "reading",
        title = "Reading",
        subtitle = "Comprehension & Vocabulary",
        iconEmoji = "📖",
        description = "Read passages, analyze contexts, and answer critical comprehension questions."
    ),
    LISTENING(
        id = "listening",
        title = "Listening",
        subtitle = "Audio & Comprehension",
        iconEmoji = "🎧",
        description = "Listen to conversations, lectures, and announcements with native English pronunciation."
    ),
    SPEAKING(
        id = "speaking",
        title = "Speaking",
        subtitle = "Pronunciation & Fluency",
        iconEmoji = "🎙️",
        description = "Speak directly into the microphone. Remember: English Trick No Speak No Trick!"
    ),
    WRITING(
        id = "writing",
        title = "Writing",
        subtitle = "Structure & Expression",
        iconEmoji = "✍️",
        description = "Practice sentence building, grammar restructuring, and expressive paragraph composition."
    )
}
