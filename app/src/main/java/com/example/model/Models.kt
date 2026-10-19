package com.example.model

data class Lesson(
    val id: Int,
    val title: String,
    val category: String,
    val bossText: String,
    val leaderText: String,
    val scenario: String,
    val takeaway: String,
    val readMinutes: Int,
    var isCompleted: Boolean,
    var isBookmarked: Boolean
)

data class QuizQuestion(
    val id: Int,
    val question: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctOption: Int, // 0 to 3
    val explanation: String,
    val category: String
)

data class QuizResult(
    val id: Int,
    val score: Int,
    val total: Int,
    val takenAt: Long
)

data class UserStats(
    val streakDays: Int,
    val lastActiveDate: String,
    val themeMode: String // "SYSTEM", "LIGHT", "DARK"
)
