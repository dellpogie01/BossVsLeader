package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    val view = LocalView.current
    DisposableEffect(view) {
        val window = (view.context as? android.app.Activity)?.window
        val systemBars = window?.let { androidx.core.view.WindowInsetsControllerCompat(it, view) }
        val wasLightStatusBar = systemBars?.isAppearanceLightStatusBars
        val wasLightNavigationBar = systemBars?.isAppearanceLightNavigationBars
        val wasNavigationBarContrastEnforced = if (android.os.Build.VERSION.SDK_INT >= 29) {
            window?.isNavigationBarContrastEnforced
        } else {
            null
        }
        systemBars?.apply {
            isAppearanceLightStatusBars = true
            isAppearanceLightNavigationBars = true
        }
        if (android.os.Build.VERSION.SDK_INT >= 29) {
            window?.isNavigationBarContrastEnforced = false
        }
        onDispose {
            if (wasLightStatusBar != null) systemBars?.isAppearanceLightStatusBars = wasLightStatusBar
            if (wasLightNavigationBar != null) systemBars?.isAppearanceLightNavigationBars = wasLightNavigationBar
            if (wasNavigationBarContrastEnforced != null && android.os.Build.VERSION.SDK_INT >= 29) {
                window?.isNavigationBarContrastEnforced = wasNavigationBarContrastEnforced
            }
        }
    }

    var startAnimation by remember { mutableStateOf(false) }

    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1500),
        label = "alpha"
    )

    val scaleAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1.1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    val progressAnim = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        startAnimation = true
        progressAndNavigate(progressAnim, onTimeout)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.playstore_icon),
                contentDescription = "BossVsLeader",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(168.dp)
                    .scale(scaleAnim.value)
                    .alpha(alphaAnim.value)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "BossVsLeader",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 36.sp
                ),
                modifier = Modifier.alpha(alphaAnim.value)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Lead. Don't just boss.",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.Black,
                    fontSize = 18.sp
                ),
                modifier = Modifier.alpha(alphaAnim.value)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Progress bar
            LinearProgressIndicator(
                progress = { progressAnim.value },
                modifier = Modifier
                    .width(200.dp)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = Color.Black,
                trackColor = Color(0xFFE0E0E0),
            )
        }
    }
}

private suspend fun progressAndNavigate(
    progressAnim: Animatable<Float, AnimationVector1D>,
    onTimeout: () -> Unit
) {
    progressAnim.animateTo(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 3000, easing = LinearEasing)
    )
    delay(100)
    onTimeout()
}
