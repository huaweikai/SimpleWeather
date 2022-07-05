package com.hua.simpleweather.network.bean

import com.google.gson.annotations.SerializedName
import java.util.*

data class WeatherVO(
    val status:String,
    val result:Result
){
    data class Result(val realtime: Realtime,val daily: Daily)

    data class Realtime(
        val skycon: String, val temperature: Float,
        @SerializedName("air_quality") val airQuality: AirQuality
    )

    data class AirQuality(val pm25:Int)

    data class Daily(
        val temperature: List<Temperature>, val skycon: List<Skycon>,
        val astro: List<Astro>,
        @SerializedName("life_index") val index: Index
    )

    data class Temperature(val max: Float, val min: Float)

    data class Astro(val date: Date, val sunrise:Time, val sunset:Time)

    data class Time(val time:String)

    data class Skycon(val value: String, val date: Date)

    data class Index(
        val coldRisk: List<LifeDescription>, val carWashing: List<LifeDescription>,
        val ultraviolet: List<LifeDescription>, val dressing: List<LifeDescription>
    )

    data class LifeDescription(val desc: String)
}