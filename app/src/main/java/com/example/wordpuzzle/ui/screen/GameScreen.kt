package com.example.wordpuzzle.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wordpuzzle.ui.components.CrosswordGrid
import com.example.wordpuzzle.ui.components.LetterWheel
import com.example.wordpuzzle.viewmodel.GameViewModel
import com.example.wordpuzzle.viewmodel.WordResult

@Composable
fun GameScreen(
    levelId: Int,
    onBack: () -> Unit,
    onLevelComplete: (Int) -> Unit
) {
    val viewModel: GameViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isLevelComplete) {
        if (uiState.isLevelComplete) {
            onLevelComplete(levelId + 1)
        }
    }

    val feedbackColor = when (uiState.lastWordResult) {
        WordResult.VALID_GRID -> Color(0xFF4CAF50)
        WordResult.INVALID -> Color(0xFFF44336)
        WordResult.NONE -> Color.Transparent
    }

    val feedbackText = when (uiState.lastWordResult) {
        WordResult.VALID_GRID -> "✓ Great!"
        WordResult.INVALID -> "✗ Try Again"
        WordResult.NONE -> ""
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F1B33))
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Level $levelId",
                color = Color(0xFFB0BEC5),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(24.dp))

            val level = uiState.level
            if (level != null) {
                CrosswordGrid(
                    gridCells = level.grid,
                    foundWords = uiState.foundGridWords,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            AnimatedVisibility(
                visible = uiState.lastWordResult != WordResult.NONE,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .background(feedbackColor, RoundedCornerShape(20.dp))
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = feedbackText,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            val level2 = uiState.level
            if (level2 != null) {
                LetterWheel(
                    letters = level2.letters,
                    selectedIndices = uiState.selectedIndices,
                    currentWord = uiState.currentWord,
                    onSelectionChanged = { viewModel.onLetterSelected(it) },
                    onSwipeComplete = { viewModel.onSwipeComplete() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .padding(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        if (uiState.isLevelComplete) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x99000000)),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2A4A)),
                    modifier = Modifier.padding(32.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🎉", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Level Complete!",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { onLevelComplete(levelId + 1) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF5A623)
                            ),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = "Next Level →",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

    }
}