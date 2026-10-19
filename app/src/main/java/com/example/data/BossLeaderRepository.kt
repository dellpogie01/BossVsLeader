package com.example.data

import android.content.Context
import com.example.model.Lesson
import com.example.model.QuizQuestion
import com.example.model.QuizResult
import com.example.model.UserStats
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BossLeaderRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    suspend fun getLessons(): List<Lesson> = withContext(Dispatchers.IO) {
        dbHelper.getAllLessons()
    }

    suspend fun updateCompletion(id: Int, completed: Boolean) = withContext(Dispatchers.IO) {
        dbHelper.updateLessonCompletion(id, completed)
    }

    suspend fun updateBookmark(id: Int, bookmarked: Boolean) = withContext(Dispatchers.IO) {
        dbHelper.updateLessonBookmark(id, bookmarked)
    }

    suspend fun getQuizQuestions(): List<QuizQuestion> = withContext(Dispatchers.IO) {
        dbHelper.getAllQuizQuestions()
    }

    suspend fun saveQuizResult(score: Int, total: Int) = withContext(Dispatchers.IO) {
        dbHelper.saveQuizResult(score, total)
    }

    suspend fun getQuizResults(): List<QuizResult> = withContext(Dispatchers.IO) {
        dbHelper.getQuizResults()
    }

    suspend fun getUserStats(): UserStats = withContext(Dispatchers.IO) {
        dbHelper.getUserStats()
    }

    suspend fun updateUserStats(streak: Int, theme: String) = withContext(Dispatchers.IO) {
        dbHelper.updateUserStats(streak, theme)
    }

    suspend fun resetProgress() = withContext(Dispatchers.IO) {
        dbHelper.resetProgress()
    }
}
