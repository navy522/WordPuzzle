package com.example.wordpuzzle.ui.screen

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(onPlayClicked: () -> Unit) {

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    val titleAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(800),
        label = "title_alpha"
    )
    val titleScale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.7f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "title_scale"
    )
    val buttonAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(800, delayMillis = 400),
        label = "button_alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0F1B33), Color(0xFF1B2A4A))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "WORD",
                color = Color(0xFFF5A623),
                fontSize = 52.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .alpha(titleAlpha)
                    .scale(titleScale)
            )
            Text(
                text = "PUZZLE",
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .alpha(titleAlpha)
                    .scale(titleScale)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Connect letters · Find words · Fill the grid",
                color = Color.White,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(titleAlpha)
            )

            Spacer(modifier = Modifier.height(56.dp))

            Button(
                onClick = onPlayClicked,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF5A623)
                ),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .alpha(buttonAlpha)
                    .width(180.dp)
                    .height(52.dp)
            ) {
                Text(
                    text = "PLAY",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}