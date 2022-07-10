package com.hua.resource

/**
 * 风信息，等级，介绍等的工具类
 */

fun getWind(kmH: Float): Int {
    return when (kmH) {
        in 1f..5f -> 1
        in 6f..11f -> 2
        in 12f..19f -> 3
        in 20f..28f -> 4
        in 29f..38f -> 5
        in 39f..49f -> 6
        in 50f..61f -> 7
        in 62f..74f -> 8
        in 75f..88f -> 9
        in 89f..102f -> 10
        in 103f..117f -> 11
        in 118f..133f -> 12
        in 134f..149f -> 13
        in 150f..166f -> 14
        in 167f..183f -> 15
        in 184f..201f -> 16
        in 202f..220f -> 17
        else -> 0
    }
}

fun getWindDirection(degree: Float): String? {
    if (degree < 0) return null
    return when (degree) {
        in 22.5..67.5 -> "东北风"
        in 67.6..112.5 -> "东北风"
        in 112.6..157.5 -> "东北风"
        in 157.6..202.5 -> "南风"
        in 202.6..247.5 -> "西南风"
        in 247.6..292.5 -> "西风"
        in 292.5..337.5 -> "西北风"
        else -> "北风"
    }
}