package com.hua.simpleweather.network.bean

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName


data class RealtimeResponse(
    val status: String,
    val result: Result,
    val location: List<String>
) {

    data class Result(val realtime: Realtime)

    data class Realtime(
        val skycon: String, val temperature: Float,
        @SerializedName("air_quality") val airQuality: AirQuality
    )

    data class AirQuality(val aqi: AQI)

    data class AQI(val chn: Float)
}
