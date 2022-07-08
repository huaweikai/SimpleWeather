package com.hua.network.bean

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class RealtimeResponse(
    val realtime:Realtime
){
    @Serializable
    data class Realtime(
        val skycon: String, val temperature: Float,
        @SerialName("air_quality") val airQuality: AirQuality
    )

    @Serializable
    data class AirQuality(val pm25:Int)
}
