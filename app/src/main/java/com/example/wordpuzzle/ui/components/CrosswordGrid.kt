package com.example.wordpuzzle.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordpuzzle.data.model.GridCell

private val CellEmpty = Color(0xFF1B2A4A)
private val CellRevealed = Color(0xFFF5A623)
private val CellBorder = Color(0xFF3D5A8A)
private val CellText = Color.White

@Composable
fun CrosswordGrid(
    gridCells: List<GridCell>,
    foundWords: Set<String>,
    modifier: Modifier = Modifier
) {
    val cellMap = remember(gridCells, foundWords) {
        buildCellMap(gridCells, foundWords)
    }

    if (cellMap.isEmpty()) return

    val maxRow = cellMap.keys.maxOf { it.first }
    val maxCol = cellMap.keys.maxOf { it.second }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (row in 0..maxRow) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    for (col in 0..maxCol) {
                        val cell = cellMap[Pair(row, col)]
                        if (cell != null) {
                            GridCellBox(
                                letter = cell.first,
                                isRevealed = cell.second
                            )
                        } else {
                            // Empty space — invisible placeholder
                            Spacer(modifier = Modifier.size(36.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GridCellBox(
    letter: String,
    isRevealed: Boolean
) {
    val bgColor = when {
        isRevealed -> CellRevealed
        else -> CellEmpty
    }

    var visible by remember(isRevealed) { mutableStateOf(false) }
    LaunchedEffect(isRevealed) {
        if (isRevealed) visible = true
    }

    Box(
        modifier = Modifier
            .size(36.dp)
            .background(bgColor, RoundedCornerShape(4.dp))
            .border(1.dp, CellBorder, RoundedCornerShape(4.dp)),
        contentAlignment = Alignment.Center
    ) {
        if (isRevealed) {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(300)) + scaleIn(tween(300))
            ) {
                Text(
                    text = letter,
                    color = CellText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

private fun buildCellMap(
    gridCells: List<GridCell>,
    foundWords: Set<String>
): Map<Pair<Int, Int>, Pair<String, Boolean>> {
    val map = mutableMapOf<Pair<Int, Int>, Pair<String, Boolean>>()

    gridCells.forEach { cell ->
        val isRevealed = cell.word.uppercase() in foundWords
        cell.word.forEachIndexed { index, char ->
            val pos = when (cell.direction) {
                "H" -> Pair(cell.row, cell.col + index)
                "V" -> Pair(cell.row + index, cell.col)
                else -> Pair(cell.row, cell.col + index)
            }

            val existing = map[pos]
            if (existing == null || isRevealed) {
                map[pos] = Pair(char.toString(), isRevealed)
            }
        }
    }

    return map
}