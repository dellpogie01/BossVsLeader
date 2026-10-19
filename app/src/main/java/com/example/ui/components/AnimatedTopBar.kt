package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.BossRed
import com.example.ui.theme.LeaderBlue

@Composable
fun AnimatedTopBar(
    currentRoute: String,
    streakDays: Int,
    onTabSelected: (String) -> Unit
) {
    // Infinite transition for shifting gradient background
    val infiniteTransition = rememberInfiniteTransition(label = "gradient")
    val colorOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "colorOffset"
    )

    val brush = Brush.linearGradient(
        colors = listOf(
            LeaderBlue,
            BossRed.copy(alpha = 0.8f),
            LeaderBlue.copy(alpha = 0.9f)
        ),
        start = androidx.compose.ui.geometry.Offset(colorOffset, 0f),
        end = androidx.compose.ui.geometry.Offset(0f, colorOffset)
    )

    // Flame pulse scale animation
    val pulseAnim = rememberInfiniteTransition(label = "pulse")
    val flameScale by pulseAnim.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flameScale"
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 6.dp,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .background(brush)
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Title cross-fade
                AnimatedContent(targetState = currentRoute, label = "titleCrossFade") { route ->
                    val title = when (route) {
                        "home" -> "BossVsLeader"
                        "lessons" -> "Leadership Lessons"
                        "compare" -> "Boss vs Leader"
                        "quiz" -> "Leadership Quiz"
                        "profile" -> "Your Profile"
                        else -> "BossVsLeader"
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (route == "home") {
                            Image(
                                painter = painterResource(R.drawable.playstore_icon),
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }
                }

                // Streak Badge
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White.copy(alpha = 0.2f),
                    modifier = Modifier.clickable { }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = "Streak",
                            tint = Color(0xFFFFD700),
                            modifier = Modifier
                                .size(20.dp)
                                .scale(flameScale)
                        )
                        Text(
                            text = "$streakDays Days",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Tab Row with animated pill indicator
            val tabs = listOf("home", "lessons", "compare", "quiz", "profile")
            val tabLabels = listOf("Home", "Lessons", "Compare", "Quiz", "Profile")

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.2f), RoundedCornerShape(28.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                tabs.forEachIndexed { index, route ->
                    val selected = currentRoute == route
                    val backgroundColor = if (selected) Color.White else Color.Transparent
                    val textColor = if (selected) LeaderBlue else Color.White

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(24.dp))
                            .background(backgroundColor)
                            .clickable { onTabSelected(route) }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tabLabels[index],
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                color = textColor
                            ),
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}
