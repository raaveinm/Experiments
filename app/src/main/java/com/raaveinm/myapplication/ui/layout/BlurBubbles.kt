package com.raaveinm.myapplication.ui.layout

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.asAndroidColorFilter

class BlurBubbles () {
    @RequiresApi(Build.VERSION_CODES.S)
    fun getBubblesRenderEffect(): RenderEffect {
        val radiusX = 80.0F
        val radiusY = 75.0F
        val s = 80.0F
        val t = -3500.0F

        val blur: RenderEffect =
            RenderEffect.createBlurEffect(radiusX, radiusY, Shader.TileMode.MIRROR)

        val colorFilter = androidx.compose.ui.graphics.ColorFilter.colorMatrix(
            ColorMatrix(
                floatArrayOf(
                    1f, 0f, 0f, 0.2f, 0f,
                    0f, 1f, 0f, 0f, 0f,
                    0.3f, 0f, 1f, 0f, 0f,
                    0f, 0f, 0f, s, t
                )
            )
        )

        val alphaMatrix: RenderEffect =
            RenderEffect.createColorFilterEffect(colorFilter.asAndroidColorFilter())

        return RenderEffect.createChainEffect(alphaMatrix, blur)
    }
}