package com.example.wordpuzzle.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.wordpuzzle.ui.screen.GameScreen
import com.example.wordpuzzle.ui.screen.HomeScreen
import com.example.wordpuzzle.ui.screen.LevelSelectScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object LevelSelect : Screen("level_select")
    object Game : Screen("game/{levelId}") {
        fun createRoute(levelId: Int) = "game/$levelId"
    }
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onPlayClicked = {
                    navController.navigate(Screen.LevelSelect.route)
                }
            )
        }

        composable(Screen.LevelSelect.route) {
            LevelSelectScreen(
                onLevelSelected = { levelId ->
                    navController.navigate(Screen.Game.createRoute(levelId))
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.Game.route,
            arguments = listOf(navArgument("levelId") { type = NavType.IntType })
        ) { backStackEntry ->
            val levelId = backStackEntry.arguments?.getInt("levelId") ?: 0
            GameScreen(
                levelId = levelId,
                onBack = { navController.popBackStack() },
                onLevelComplete = { nextLevelId ->
                    navController.navigate(Screen.Game.createRoute(nextLevelId)) {
                        popUpTo(Screen.Game.route) { inclusive = true }
                    }
                }
            )
        }
    }
}