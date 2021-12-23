package com.hua.simpleweather.network.bean



data class Weather(
    val realtimeresponse: RealtimeResponse?,
    val dailyresponse: DailyResponse?,
    val cityName: String=""
)
