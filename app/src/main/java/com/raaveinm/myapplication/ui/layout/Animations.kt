package com.raaveinm.myapplication.ui.layout

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath



/*
 *  Octagon Figure
 */
@Composable
fun OctagonFigure(
    modifier: Modifier = Modifier)
{
    Box (
        modifier = Modifier.drawWithCache() {
            val roundedPolygon = RoundedPolygon(
                numVertices = 8,
                radius = size.minDimension / 7,
                centerX = size.width/2,
                centerY = size.height/2,
            )
            val roundedPolygonPath = roundedPolygon.toPath().asComposePath()
            onDrawBehind {
                drawPath(
                    path = roundedPolygonPath,
                    color = Color(0xFF673AB7)
                )
            }
        }.fillMaxSize()
    ){

    }
}

@Composable
fun RoundTriangle(){
    Box(
        modifier = Modifier
            .drawWithCache {
                val roundedPolygon = RoundedPolygon(
                    numVertices = 3,
                    radius = size.minDimension / 2,
                    centerX = size.width / 2,
                    centerY = size.height / 2,
                    rounding = CornerRounding(
                        size.minDimension / 10f,
                        smoothing = 0.1f
                    )
                )
                val roundedPolygonPath = roundedPolygon.toPath().asComposePath()
                onDrawBehind {
                    drawPath(roundedPolygonPath, color = Color.Black)
                }
            }
            .size(100.dp)
    )
}


@Preview
@Composable
fun OctagonPreview(){
    OctagonFigure(modifier = Modifier.fillMaxSize())
}

@Preview
@Composable
fun RoundTrianglePreview(){
    RoundTriangle()
}