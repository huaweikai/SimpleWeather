package com.hua.resource

import android.graphics.Color
import androidx.annotation.ColorInt

fun getWarnIcon(
    code:Int
):Int{
    return when(code / 100){
        1->R.drawable.ic_warn_typhoon
        2->R.drawable.ic_warn_rainstorm
        3->R.drawable.ic_warn_blizzard
        4->R.drawable.ic_warn_cold_wave
        5->R.drawable.ic_warn_windy
        6->R.drawable.ic_warn_dust_strom
        7->R.drawable.ic_warn_high_temp
        8->R.drawable.ic_warn_dry
        9->R.drawable.ic_warn_thunder
        10->R.drawable.ic_warn_hailstome
        11->R.drawable.ic_warn_frost
        12->R.drawable.ic_warn_dense_fog
        13->R.drawable.ic_warn_haze
        14->R.drawable.ic_warn_icy_road
        15->R.drawable.ic_warn_forest_fire
        else->R.drawable.ic_warn_thunder
    }
}

fun getWarnDec(
    code:Int
):String{
    val warn = when(code / 100){
        1-> "台风"
        2-> "暴雨"
        3-> "暴雪"
        4-> "寒潮"
        5-> "大风"
        6-> "沙尘暴"
        7-> "高温"
        8-> "干旱"
        9-> "雷电"
        10-> "冰雹"
        11-> "霜冻"
        12-> "大雾"
        13-> "雾霾"
        14-> "道路结冰"
        15-> "森林火灾"
        else-> "雷雨大风"
    }
    val level = when(code % 100){
        1->"蓝色"
        2->"黄色"
        3->"橙色"
        else -> "红色"
    }
    return warn + level
}

@ColorInt
private val ORANGE = -0x5b00

fun getWarnColor(
    code: Int
):Int{
    return when(code % 100){
        1-> Color.BLUE
        2->Color.YELLOW
        3-> ORANGE
        else -> Color.RED
    }
}
