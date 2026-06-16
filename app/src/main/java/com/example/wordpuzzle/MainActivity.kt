package com.example.wordpuzzle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.wordpuzzle.navigation.NavGraph
import com.example.wordpuzzle.ui.theme.WordPuzzleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WordPuzzleTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}