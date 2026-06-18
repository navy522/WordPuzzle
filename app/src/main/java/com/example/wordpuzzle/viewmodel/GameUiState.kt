package com.example.wordpuzzle.viewmodel

import com.example.wordpuzzle.data.model.Level

data class GameUiState(
    val level: Level? = null,
    val currentWord: String = "",
    val selectedIndices: List<Int> = emptyList(),
    val foundGridWords: Set<String> = emptySet(),
    val isLevelComplete: Boolean = false,
    val lastWordResult: WordResult = WordResult.NONE
)

enum class WordResult {
    NONE,
    VALID_GRID,
    INVALID
}