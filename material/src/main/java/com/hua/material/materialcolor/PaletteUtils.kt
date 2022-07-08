package com.hua.material.materialcolor

import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.graphics.ColorUtils
import androidx.palette.graphics.Palette
import com.hua.material.materialcolor.quantize.QuantizerCelebi
import com.hua.material.materialcolor.scheme.Scheme
import com.hua.material.materialcolor.score.ContainerScore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.max
import kotlin.math.min

/**
 *
 * @author Xiaoc
 * @since 2021/1/10
 *
 * 调色板工具
 * 负责解析Bitmap色调
 */
object PaletteUtils {

    fun resolveByBitmap(bitmap: Bitmap, isDarkMode: Boolean): ColorContainerData {
//        return withContext(Dispatchers.Default){
//            resolveByPixelsInner(bitmapToPixels(bitmap), isDarkMode)
//        }
        return resolveByPixelsInner(bitmapToPixels(bitmap), isDarkMode)
    }

    fun resolveByPixels(pixels: IntArray, isDarkMode: Boolean): ColorContainerData {
        return resolveByPixelsInner(pixels, isDarkMode)

    }

    private fun resolveByPixelsInner(pixels: IntArray, isDarkMode: Boolean): ColorContainerData {
        return if(isDarkMode){
            val quantize = QuantizerCelebi.quantize(pixels, 10)
            val primaryColorPair = ContainerScore.score(quantize, false)
            if(primaryColorPair.first){
                val colors = primaryColorPair.second[0]
                ColorContainerData(rgb2Argb(colors), colors)
            } else {
                val colors = Scheme.dark(primaryColorPair.second[0])
                ColorContainerData(rgb2Argb(colors.primary), colors.primary)
            }
        } else {
            val quantize = QuantizerCelebi.quantize(pixels, 10)
            val primaryColorPair = ContainerScore.score(quantize, true)
            if(primaryColorPair.first){
                val colors = primaryColorPair.second[0]
                ColorContainerData(rgb2Argb(colors), colors)
            } else {
                val colors = Scheme.light(primaryColorPair.second[0])
                ColorContainerData(rgb2Argb(colors.primary), colors.primary)
            }
        }
    }

    private fun bitmapToPixels(bitmap: Bitmap): IntArray{
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        return pixels
    }

    /**
     * 解析Bitmap的色调
     * 选出该Bitmap最合适在 白色/黑色 面板上展示的颜色
     *
     * 当是非深色模式时，会寻找 hsl 亮度在0.26-0.85的色调，如果不存在再选择主色调，并进行一定处理
     * 当为深色模式时，会寻找 hsl 亮度在0.26-0.85的色调，如果不存在再选择主色调，并进行一定处理
     * 如果都不存在，使用传来的默认颜色
     */
    @Deprecated("由于Palette配色方案不再适用于Material3，所以使用新的配色方案")
    suspend fun resolveBitmap(isDarkMode: Boolean,bitmap: Bitmap,defaultColor: Int): Pair<Int,Int>{
        return withContext(Dispatchers.Default){

            val palette = Palette.from(bitmap).addFilter { _, hsl ->
                hsl[2] in 0.26..0.85
            }.generate()

            return@withContext palette.dominantSwatch?.let { swatch ->
                if(isDarkMode){
                    handleDarkModeColor(swatch)
                } else {
                    handleLightModeColor(swatch)
                }
            } ?: Pair(defaultColor, rgb2Argb(defaultColor))
        }
    }

    /**
     * 处理亮色颜色，将其调整为放在浅色板上能看清的颜色
     * 当 H 和 S 都为 0 时，代表此颜色偏浅灰，如果亮度再高于 0.55，需要调整其亮度
     * 当 L 高于 0.60 后，可能会导致看不清楚高亮色，将其调低
     */
    private fun handleLightModeColor(swatch: Palette.Swatch): Pair<Int,Int>{

        val hsl = swatch.hsl

        if(hsl[0] == 0f && hsl[1] == 0f && hsl[2] > 0.55){
            hsl[2] = min(0.55f,hsl[2]-0.2f)
        }
        if(hsl[2] > 0.60f){
            hsl[2] = min(0.55f,hsl[2]-0.25f)
        }
        if(hsl[1] > 0.80f){
            hsl[1] = min(0.7f,hsl[1]-0.2f)
        }
        val primaryColor = ColorUtils.HSLToColor(hsl)
        // Logger.d("颜色值：H:${hsl[0]} S:${hsl[1]} L:${hsl[2]}")
        return Pair(primaryColor, rgb2Argb(primaryColor))
    }

    /**
     * 处理暗色颜色，将其调整为放在深色板上能看清的颜色
     * 统一将亮度调整为最高 1，最低 0.85
     */
    private fun handleDarkModeColor(swatch: Palette.Swatch): Pair<Int,Int>{
        val hsl = swatch.hsl

        if(hsl[2] < 0.7f){
            hsl[2] = max(0.85f,min(hsl[2] + 0.2f,1f))
        }

        val primaryColor = ColorUtils.HSLToColor(hsl)
        return Pair(primaryColor, rgb2Argb(primaryColor))
    }

    fun rgb2Argb(color: Int): Int{
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        return Color.argb(46,red,green,blue)
    }
}

data class ColorContainerData(
    val containerColor: Int,
    val primaryColor: Int
)