package com.raaveinm.myapplication.ui.layout

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.random.Random

data class BubbleState(
    val id: Int,
    val initialX: Float,
    val initialY: Float,
    val targetX: Float,
    val targetY: Float,
    val size: Dp,
    val color: Color,
    val duration: Int
)

class BubbleAnimation(
    val animatedX: Animatable<Float, androidx.compose.animation.core.AnimationVector1D>,
    val animatedY: Animatable<Float, androidx.compose.animation.core.AnimationVector1D>
)

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun FloatingBubblesScreen(onClick: () -> Unit = {}) {
    val blurBubbles = remember { BlurBubbles() }
    val bubbleCount = 80
    val bubbles = remember { mutableStateListOf<BubbleState>() }
    val density = LocalDensity.current

    BoxWithConstraints(modifier = Modifier.fillMaxSize().pointerInput(Unit){ detectTapGestures { onClick() } }) {
        val maxWidthPx = with(density) { maxWidth.toPx() }
        val maxHeightPx = with(density) { maxHeight.toPx() }

        LaunchedEffect(Unit) {
            if (bubbles.isEmpty()) {
                repeat(bubbleCount) { index ->
                    bubbles.add(
                        BubbleState(
                            id = index,
                            initialX = Random.nextFloat() * maxWidthPx,
                            initialY = Random.nextFloat() * maxHeightPx,
                            targetX = Random.nextFloat() * maxWidthPx,
                            targetY = Random.nextFloat() * maxHeightPx,
                            size = Random.nextInt(20, 60).dp,
                            color = Color(
                                red = Random.nextInt(150, 255),
                                green = Random.nextInt(150, 255),
                                blue = Random.nextInt(150, 255),
                                alpha = Random.nextInt(100, 200)
                            ),
                            duration = Random.nextInt(5000, 150000)
                        )
                    )
                }
            }
        }
        val bubbleAnimations = remember { mutableMapOf<Int, BubbleAnimation>() }
        bubbles.forEach { bubble ->
            if (!bubbleAnimations.containsKey(bubble.id)) {
                bubbleAnimations[bubble.id] = BubbleAnimation(
                    remember(bubble.id) { Animatable(bubble.initialX) },
                    remember(bubble.id) { Animatable(bubble.initialY) }
                )
            }
        }
        bubbleAnimations.forEach { (bubbleId, animation) ->
            val bubble = bubbles.find { it.id == bubbleId } ?: return@forEach
            LaunchedEffect(bubble) {
                launch {
                    animation.animatedX.animateTo(
                        bubble.targetX,
                        infiniteRepeatable(
                            tween(bubble.duration, easing = LinearEasing),
                            RepeatMode.Reverse
                        )
                    )
                }
                launch {
                    animation.animatedY.animateTo(
                        bubble.targetY,
                        infiniteRepeatable(
                            tween(bubble.duration, easing = LinearEasing),
                            RepeatMode.Reverse
                        )
                    )
                }
            }
        }

        val modifierWithEffect = Modifier.fillMaxSize()
            .then(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    Modifier.graphicsLayer {
                        renderEffect =
                            blurBubbles.getBubblesRenderEffect().asComposeRenderEffect()
                        clip = true
                    }
                } else {
                    Modifier
                }
            )


        Canvas(modifier = modifierWithEffect) {
            bubbles.forEach { bubble ->
                val animation = bubbleAnimations[bubble.id] ?: return@forEach

                // Draw the bubble
                drawCircle(
                    color = bubble.color,
                    radius = with(density) { bubble.size.toPx() } / 2f,
                    center = Offset(animation.animatedX.value, animation.animatedY.value)
                )
            }
        }
    }
}