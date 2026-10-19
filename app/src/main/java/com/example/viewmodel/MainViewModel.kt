package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.BossLeaderRepository
import com.example.model.Lesson
import com.example.model.QuizQuestion
import com.example.model.QuizResult
import com.example.model.UserStats
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = BossLeaderRepository(application)

    private val _lessons = MutableStateFlow<List<Lesson>>(emptyList())
    val lessons: StateFlow<List<Lesson>> = _lessons.asStateFlow()

    private val _quizQuestions = MutableStateFlow<List<QuizQuestion>>(emptyList())
    val quizQuestions: StateFlow<List<QuizQuestion>> = _quizQuestions.asStateFlow()

    private val _quizResults = MutableStateFlow<List<QuizResult>>(emptyList())
    val quizResults: StateFlow<List<QuizResult>> = _quizResults.asStateFlow()

    private val _userStats = MutableStateFlow(UserStats(5, "2026-10-19", "SYSTEM"))
    val userStats: StateFlow<UserStats> = _userStats.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            val fetchedLessons = repository.getLessons()
            val fetchedQuiz = repository.getQuizQuestions()
            val fetchedResults = repository.getQuizResults()
            val stats = repository.getUserStats()

            _lessons.value = fetchedLessons
            _quizQuestions.value = fetchedQuiz
            _quizResults.value = fetchedResults
            _userStats.value = stats
        }
    }

    fun toggleCompletion(lessonId: Int) {
        viewModelScope.launch {
            val currentList = _lessons.value.toMutableList()
            val index = currentList.indexOfFirst { it.id == lessonId }
            if (index != -1) {
                val updated = currentList[index].copy(isCompleted = !currentList[index].isCompleted)
                currentList[index] = updated
                _lessons.value = currentList
                repository.updateCompletion(lessonId, updated.isCompleted)
            }
        }
    }

    fun toggleBookmark(lessonId: Int) {
        viewModelScope.launch {
            val currentList = _lessons.value.toMutableList()
            val index = currentList.indexOfFirst { it.id == lessonId }
            if (index != -1) {
                val updated = currentList[index].copy(isBookmarked = !currentList[index].isBookmarked)
                currentList[index] = updated
                _lessons.value = currentList
                repository.updateBookmark(lessonId, updated.isBookmarked)
            }
        }
    }

    fun setSelectedCategory(category: String) {
        _selectedCategory.value = category
    }

    fun submitQuizScore(score: Int, total: Int, onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.saveQuizResult(score, total)
            val updatedResults = repository.getQuizResults()
            _quizResults.value = updatedResults
            onComplete()
        }
    }

    fun setThemeMode(mode: String) {
        viewModelScope.launch {
            repository.updateUserStats(_userStats.value.streakDays, mode)
            _userStats.value = _userStats.value.copy(themeMode = mode)
        }
    }

    fun resetAllProgress(onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.resetProgress()
            loadData()
            onComplete()
        }
    }
}
