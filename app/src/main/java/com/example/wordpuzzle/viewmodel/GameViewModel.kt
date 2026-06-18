package com.example.wordpuzzle.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.wordpuzzle.data.LevelRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val repository = LevelRepository(application)
    private val levelId: Int = savedStateHandle["levelId"] ?: 1

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    init {
        loadLevel(levelId)
    }

    private fun loadLevel(id: Int) {
        val level = repository.getLevel(id)
        _uiState.update { it.copy(level = level) }
    }

    fun onLetterSelected(indices: List<Int>) {
        val level = _uiState.value.level ?: return
        val word = indices.joinToString("") { level.letters[it] }
        _uiState.update { it.copy(selectedIndices = indices, currentWord = word) }
    }

    fun onSwipeComplete() {
        val state = _uiState.value
        val level = state.level ?: return

        val word = state.selectedIndices
            .joinToString("") { level.letters[it] }
            .uppercase()

        if (word.isEmpty()) return

        when {
            word in level.gridWords && word !in state.foundGridWords -> {
                val newFound = state.foundGridWords + word
                val isComplete = newFound.size == level.gridWords.toSet().size
                _uiState.update {
                    it.copy(
                        foundGridWords = newFound,
                        lastWordResult = WordResult.VALID_GRID,
                        isLevelComplete = isComplete,
                        currentWord = "",
                        selectedIndices = emptyList()
                    )
                }
            }
            else -> {
                _uiState.update {
                    it.copy(
                        lastWordResult = WordResult.INVALID,
                        currentWord = "",
                        selectedIndices = emptyList()
                    )
                }
            }
        }

        viewModelScope.launch {
            delay(600)
            _uiState.update {
                it.copy(lastWordResult = WordResult.NONE)
            }
        }
    }
}