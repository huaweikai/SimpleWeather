package com.hua.model.weather

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.math.ln

@Entity(tableName = "weather")
@Serializable
data class WeatherVO(
    val status: String,
    val location: List<String>,
    @Embedded
    val result: Result,
    @PrimaryKey
    val id: Int = 0,
    val lng: String = location[1],
    val lat: String = location[0],
    val address: String = lng + lat,
    val cityName: String = "",
    val updateTime: Long = System.currentTimeMillis()
) {
    @Serializable
    data class Result(
        @Embedded("real_")
        val realtime: Realtime,
        @Embedded("daily_")
        val daily: Daily,
        @Embedded("alert_")
        val alert: Alert,
        @SerialName("forecast_keypoint")
        val survey: String,
        @Embedded("hour_")
        val hourly: Hourly
    ) {

        @Serializable
        data class Realtime(
            val skycon: String,
            val temperature: Float,
            @SerialName("apparent_temperature")
            val parentTemperature: Float,
            @SerialName("air_quality")
            @Embedded("air_")
            val airQuality: AirQuality
        )

        @Serializable
        data class AirQuality(
            val pm25: Int, val pm10: Int, val o3: Int, val so2: Int, val no2: Int,
            val co: Float,
            @Embedded("aqi_")
            val aqi: AQI,
            @Embedded("desc_")
            val description: Description
        )

        @Serializable
        data class AQI(val chn: Int)

        @Serializable
        data class Description(val chn: String)

        @Serializable
        data class Daily(
            val temperature: List<Temperature>, val skycon: List<Skycon>,
            val astro: List<Astro>,
            @SerialName("life_index")
            @Embedded("index_")
            val index: Index
        )

        @Serializable
        data class Temperature(val max: Float, val min: Float)

        @Serializable
        data class Astro(
            val date: String,
            @Embedded("time_") val sunrise: Time,
            @Embedded("time_") val sunset: Time
        )

        @Serializable
        data class Time(val time: String)

        @Serializable
        data class Skycon(val value: String, val date: String)

        @Serializable
        data class Index(
            val coldRisk: List<LifeDescription>, val carWashing: List<LifeDescription>,
            val ultraviolet: List<LifeDescription>, val dressing: List<LifeDescription>
        )

        @Serializable
        data class LifeDescription(val desc: String)

        @Serializable
        data class Alert(val status: String, val content: List<Content>)

        @Serializable
        data class Content(
            val code: Int,
            @SerialName("pubtimestamp") val publishTime: Long,
            val description: String
        )

        @Serializable
        data class Hourly(
            @SerialName("description")
            val desc: String,

            val precipitation: List<Data>,
            val temperature: List<Data>,

            val wind: List<Wind>,

            val humidity:List<Data>,
            @SerialName("cloudrate")
            val cloudRate:List<Data>,

            @SerialName("skycon")
            val skyIcon:List<Data>,

            @Embedded("hour_air_")
            @SerialName("air_quality")
            val airQuality: HourAirQuality
        )

        @Serializable
        data class Data(val datetime: String, val value: String)

        @Serializable
        data class Wind(val datetime: String, val speed: Float, val direction: Float)

        @Serializable
        data class HourAirQuality(val aqi:List<HourAQI>)

        @Serializable
        data class HourAQI(val datetime: String,@Embedded ("hour_")val value:HourValue)

        @Serializable
        data class HourValue(val chn: String)


    }
}