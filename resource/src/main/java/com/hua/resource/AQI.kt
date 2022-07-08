package com.hua.resource

fun getAQIDesc(aqi: String): String {
    val aqiInt = aqi.toIntOrNull()
    if (aqiInt == null || aqiInt < 0) return "未知"
    return when (aqiInt) {
        in 0..50 -> "优"
        in 51..100 -> "良"
        in 101..150 -> "轻度污染"
        in 151..200 -> "中度污染"
        in 201..300 -> "重度污染"
        else -> "严重污染"
    }
}