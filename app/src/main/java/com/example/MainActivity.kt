package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.components.AnimatedTopBar
import com.example.ui.screens.*
import com.example.ui.theme.BossVsLeaderTheme
import com.example.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val userStats by viewModel.userStats.collectAsStateWithLifecycle()
            val darkTheme = when (userStats.themeMode) {
                "DARK" -> true
                "LIGHT" -> false
                else -> androidx.compose.foundation.isSystemInDarkTheme()
            }

            BossVsLeaderTheme(darkTheme = darkTheme) {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route ?: "splash"

                val lessons by viewModel.lessons.collectAsStateWithLifecycle()
                val quizQuestions by viewModel.quizQuestions.collectAsStateWithLifecycle()
                val quizResults by viewModel.quizResults.collectAsStateWithLifecycle()
                val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()

                val showTopBar = currentRoute != "splash" && !currentRoute.startsWith("lesson_detail")

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = if (currentRoute == "splash") {
                        androidx.compose.ui.graphics.Color.White
                    } else {
                        androidx.compose.material3.MaterialTheme.colorScheme.background
                    },
                    topBar = {
                        if (showTopBar) {
                            AnimatedTopBar(
                                currentRoute = currentRoute,
                                streakDays = userStats.streakDays,
                                onTabSelected = { route ->
                                    if (route != currentRoute) {
                                        navController.navigate(route) {
                                            popUpTo("home") { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        NavHost(
                            navController = navController,
                            startDestination = "splash"
                        ) {
                            composable("splash") {
                                SplashScreen(
                                    onTimeout = {
                                        navController.navigate("home") {
                                            popUpTo("splash") { inclusive = true }
                                        }
                                    }
                                )
                            }
                            composable("home") {
                                HomeScreen(
                                    lessons = lessons,
                                    userStats = userStats,
                                    onLessonClick = { lessonId ->
                                        navController.navigate("lesson_detail/$lessonId")
                                    },
                                    onNavigate = { route ->
                                        navController.navigate(route) {
                                            popUpTo("home") { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                            composable("lessons") {
                                LessonsScreen(
                                    lessons = lessons,
                                    selectedCategory = selectedCategory,
                                    onCategorySelected = { viewModel.setSelectedCategory(it) },
                                    onLessonClick = { lessonId ->
                                        navController.navigate("lesson_detail/$lessonId")
                                    },
                                    onToggleCompletion = { lessonId ->
                                        viewModel.toggleCompletion(lessonId)
                                    }
                                )
                            }
                            composable(
                                route = "lesson_detail/{lessonId}",
                                arguments = listOf(navArgument("lessonId") { type = NavType.IntType })
                            ) { backStackEntry ->
                                val lessonId = backStackEntry.arguments?.getInt("lessonId") ?: 1
                                val lesson = lessons.find { it.id == lessonId }
                                LessonDetailScreen(
                                    lesson = lesson,
                                    onBack = { navController.popBackStack() },
                                    onToggleBookmark = { id -> viewModel.toggleBookmark(id) },
                                    onToggleCompletion = { id -> viewModel.toggleCompletion(id) }
                                )
                            }
                            composable("compare") {
                                CompareScreen(lessons = lessons)
                            }
                            composable("quiz") {
                                QuizScreen(
                                    questions = quizQuestions,
                                    onSubmitScore = { score, total, onComplete ->
                                        viewModel.submitQuizScore(score, total, onComplete)
                                    }
                                )
                            }
                            composable("profile") {
                                ProfileScreen(
                                    lessons = lessons,
                                    quizResults = quizResults,
                                    userStats = userStats,
                                    onLessonClick = { lessonId ->
                                        navController.navigate("lesson_detail/$lessonId")
                                    },
                                    onThemeChanged = { mode ->
                                        viewModel.setThemeMode(mode)
                                    },
                                    onResetProgress = {
                                        viewModel.resetAllProgress {}
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
