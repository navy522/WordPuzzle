package com.example.wordpuzzle.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import kotlin.math.*

private val WheelBg = Color(0xFF1B2A4A)
private val WheelRing = Color(0xFF2E4A7A)
private val LetterCircleBg = Color(0xFF2E4A7A)
private val LetterCircleSelected = Color(0xFFF5A623)
private val LetterTextColor = Color.White
private val LineColor = Color(0xFFF5A623)
private val CenterCircleBg = Color(0xFF243660)


/*
* Displays a characters inside wheel
* */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LetterWheel(
    letters: List<String>,
    selectedIndices: List<Int>,
    currentWord: String,
    onSelectionChanged: (List<Int>) -> Unit,
    onSwipeComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var hoveredIndex by remember { mutableStateOf(-1) }
    var dragPosition by remember { mutableStateOf<Offset?>(null) }

    var widthPx by remember { mutableStateOf(0f) }
    var heightPx by remember { mutableStateOf(0f) }

    val scales = letters.indices.map { i ->
        animateFloatAsState(
            targetValue = if (i == hoveredIndex || i in selectedIndices) 1.2f else 1.0f,
            animationSpec = tween(150),
            label = "scale_$i"
        )
    }

    Box(
        modifier = modifier
            .onSizeChanged { size ->
                widthPx = size.width.toFloat()
                heightPx = size.height.toFloat()
            }
            .pointerInteropFilter { event ->
                if (widthPx == 0f) return@pointerInteropFilter false

                val offset = Offset(event.x, event.y)

                when (event.action) {
                    android.view.MotionEvent.ACTION_DOWN -> {
                        val index = hitTest(offset, letters.size, widthPx, heightPx)
                        if (index != -1) {
                            onSelectionChanged(listOf(index))
                            hoveredIndex = index
                        }
                        dragPosition = offset
                        true
                    }
                    android.view.MotionEvent.ACTION_MOVE -> {
                        dragPosition = offset
                        val index = hitTest(offset, letters.size, widthPx, heightPx)
                        if (index != -1 && index != selectedIndices.lastOrNull()) {
                            hoveredIndex = index
                            if (index !in selectedIndices) {
                                onSelectionChanged(selectedIndices + index)
                            }
                        }
                        true
                    }
                    android.view.MotionEvent.ACTION_UP,
                    android.view.MotionEvent.ACTION_CANCEL -> {
                        hoveredIndex = -1
                        dragPosition = null
                        onSwipeComplete()
                        true
                    }
                    else -> false
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val centerX = canvasWidth / 2f
            val centerY = canvasHeight / 2f
            val wheelRadius = minOf(canvasWidth, canvasHeight) / 2f * 0.82f
            val letterCircleRadius = minOf(canvasWidth, canvasHeight) * 0.09f

            // Outer ring
            drawCircle(
                color = WheelBg,
                radius = wheelRadius,
                center = Offset(centerX, centerY)
            )
            drawCircle(
                color = WheelRing,
                radius = wheelRadius,
                center = Offset(centerX, centerY),
                style = Stroke(width = 4.dp.toPx())
            )

            // Center circle
            drawCircle(
                color = CenterCircleBg,
                radius = letterCircleRadius * 1.1f,
                center = Offset(centerX, centerY)
            )

            val positions = getLetterPositions(
                letters.size, centerX, centerY, wheelRadius * 0.72f
            )

            // Lines between selected letters
            if (selectedIndices.size > 1) {
                for (i in 0 until selectedIndices.size - 1) {
                    val from = positions[selectedIndices[i]]
                    val to = positions[selectedIndices[i + 1]]
                    drawLine(
                        color = LineColor,
                        start = from,
                        end = to,
                        strokeWidth = 6.dp.toPx()
                    )
                }
            }

            if (selectedIndices.isNotEmpty() && dragPosition != null) {
                drawLine(
                    color = LineColor.copy(alpha = 0.6f),
                    start = positions[selectedIndices.last()],
                    end = dragPosition!!,
                    strokeWidth = 4.dp.toPx()
                )
            }

            // Letter circles
            positions.forEachIndexed { index, pos ->
                val isSelected = index in selectedIndices
                val scale = scales[index].value
                val radius = letterCircleRadius * scale

                drawCircle(
                    color = if (isSelected) LetterCircleSelected else LetterCircleBg,
                    radius = radius,
                    center = pos
                )
                drawCircle(
                    color = if (isSelected) Color(0xFFFFD700) else WheelRing,
                    radius = radius,
                    center = pos,
                    style = Stroke(width = 2.dp.toPx())
                )

                drawLetterText(
                    text = letters[index],
                    position = pos,
                    textSize = radius * 0.85f,
                    color = LetterTextColor
                )
            }

            // Current word in center
            if (currentWord.isNotEmpty()) {
                drawLetterText(
                    text = currentWord,
                    position = Offset(centerX, centerY),
                    textSize = letterCircleRadius * 0.6f,
                    color = Color(0xFFF5A623)
                )
            }
        }
    }
}

private fun getLetterPositions(
    count: Int,
    centerX: Float,
    centerY: Float,
    radius: Float
): List<Offset> {
    val angleStep = (2 * PI / count).toFloat()
    val startAngle = (-PI / 2).toFloat()
    return (0 until count).map { i ->
        val angle = startAngle + i * angleStep
        Offset(
            x = centerX + radius * cos(angle),
            y = centerY + radius * sin(angle)
        )
    }
}

private fun hitTest(
    touch: Offset,
    count: Int,
    width: Float,
    height: Float
): Int {
    val centerX = width / 2f
    val centerY = height / 2f
    val wheelRadius = minOf(width, height) / 2f * 0.82f
    val letterCircleRadius = minOf(width, height) * 0.09f
    val positions = getLetterPositions(count, centerX, centerY, wheelRadius * 0.72f)
    val hitRadius = letterCircleRadius * 1.6f

    positions.forEachIndexed { index, pos ->
        val dx = touch.x - pos.x
        val dy = touch.y - pos.y
        if (sqrt(dx * dx + dy * dy) <= hitRadius) return index
    }
    return -1
}

private fun DrawScope.drawLetterText(
    text: String,
    position: Offset,
    textSize: Float,
    color: Color
) {
    drawContext.canvas.nativeCanvas.apply {
        val paint = android.graphics.Paint().apply {
            this.color = color.toArgb()
            this.textSize = textSize
            this.textAlign = android.graphics.Paint.Align.CENTER
            this.isFakeBoldText = true
            this.isAntiAlias = true
        }
        val textBounds = android.graphics.Rect()
        paint.getTextBounds(text, 0, text.length, textBounds)
        val textY = position.y + textBounds.height() / 2f
        drawText(text, position.x, textY, paint)
    }
}