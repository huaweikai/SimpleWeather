package com.hua.network.bean

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

//加大括号是为了避免类型有同名冲突

data class DailyResponse(
    val daily:Daily
){
    @Serializable
    data class Daily(
        val temperature: List<Temperature>, val skycon: List<Skycon>,
        val astro: List<Astro>,
        @SerialName("life_index") val index: Index
    )
    @Serializable
    data class Temperature(val max: Float, val min: Float)
    @Serializable
    data class Astro(val date: String,val sunrise:Time,val sunset:Time)
    @Serializable
    data class Time(val time:String)
    @Serializable
    data class Skycon(val value: String, val date: String)
    @Serializable
    data class Index(
        val coldRisk: List<LifeDescription>, val carWashing: List<LifeDescription>,
        val ultraviolet: List<LifeDescription>, val dressing: List<LifeDescription>
    )
    @Serializable
    data class LifeDescription(val desc: String)
}