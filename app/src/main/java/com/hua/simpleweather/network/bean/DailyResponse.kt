package com.hua.simpleweather.network.bean

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.sql.Time
import java.util.*

//加大括号是为了避免类型有同名冲突

data class DailyResponse(val status: String, val result: Result) {

    data class Result(val daily: Daily)

    data class Daily(
        val temperature: List<Temperature>, val skycon: List<Skycon>,
        val astro: List<Astro>,
        @SerializedName("life_index") val index: Index
    )

    data class Temperature(val max: Float, val min: Float)

    data class Astro(val date: Date,val sunrise:Time,val sunset:Time)

    data class Time(val time:String)

    data class Skycon(val value: String, val date: Date)

    data class Index(
        val coldRisk: List<LifeDescription>, val carWashing: List<LifeDescription>,
        val ultraviolet: List<LifeDescription>, val dressing: List<LifeDescription>
    )

    data class LifeDescription(val desc: String)
}