package edu.ucne.proyecto_final.presentation.usuario

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.ucne.proyecto_final.R
import kotlinx.coroutines.delay

@Composable
fun StartScreen(
    onSplashComplete: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.3f,
        animationSpec = tween(durationMillis = 900)
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(2200)
        onSplashComplete()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        // Fondo degradado verde-blanco
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFE8F5E9), // verde muy claro
                            Color.White
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            // Glow verde detrás del logo
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .blur(50.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF4CAF50).copy(alpha = 0.5f),
                                Color.Transparent
                            )
                        )
                    )
            )

            // Logo animado con escala y borde redondeado
            Box(
                modifier = Modifier
                    .size(170.dp)
                    .scale(scale)
                    .background(
                        Color(0xFF81C784).copy(alpha = 0.20f),
                        RoundedCornerShape(35)
                    )
                    .padding(25.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.tienda),
                    contentDescription = "Logo",
                    modifier = Modifier.size(110.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StartScreenPreview() {
    StartScreen(onSplashComplete = {})
}
