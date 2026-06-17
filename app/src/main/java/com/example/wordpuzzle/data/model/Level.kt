package com.example.wordpuzzle.data.model

data class Level(
    val id: Int,
    val letters: List<String>,
    val gridWords: List<String>,
    val grid: List<GridCell>
)

data class GridCell(
    val word: String,
    val row: Int,
    val col: Int,
    val direction: String
)