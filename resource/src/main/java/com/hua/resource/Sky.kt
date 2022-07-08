package com.hua.resource

private val skyIcon = mapOf(
    "CLEAR_DAY" to R.drawable.ic_sunny,
    "CLEAR_NIGHT" to R.drawable.ic_yewan_qing,
    "PARTLY_CLOUDY_DAY" to R.drawable.ic_cloud,
    "PARTLY_CLOUDY_NIGHT" to R.drawable.ic_cloudy_night,
    "CLOUDY" to R.drawable.ic_overcast,
    "WIND" to R.drawable.ic_nodata,
    "LIGHT_RAIN" to R.drawable.ic_small_rain,
    "MODERATE_RAIN" to R.drawable.ic_middle_rain,
    "HEAVY_RAIN" to R.drawable.ic_big_rain,
    "STORM_RAIN" to R.drawable.ic_big_rain,
    "THUNDER_SHOWER" to R.drawable.ic_thunder_rain,
    "SLEET" to R.drawable.ic_rain_snow,
    "LIGHT_SNOW" to R.drawable.ic_light_snow,
    "MODERATE_SNOW" to R.drawable.ic_moderate_snow,
    "HEAVY_SNOW" to R.drawable.ic_heavy_snow,
    "STORM_SNOW" to R.drawable.ic_heavy_snow,
    "HAIL" to R.drawable.ic_haze,
    "LIGHT_HAZE" to R.drawable.ic_fog,
    "MODERATE_HAZE" to R.drawable.ic_fog,
    "HEAVY_HAZE" to R.drawable.ic_fog,
    "FOG" to R.drawable.ic_fog,
    "DUST" to R.drawable.ic_fuchen
)

fun getSkyIcon(sky: String) = skyIcon[sky] ?: R.drawable.ic_sunny

fun getDayBg(sky: String?): Int {
    return when (sky) {
        "CLEAR_DAY" -> R.mipmap.back_900d
        "CLOUDY" ->R.mipmap.back_cloudy_day
        "CLEAR_NIGHT" -> R.mipmap.back_clear_night
        "PARTLY_CLOUDY_DAY" -> R.mipmap.back_partly_cloud_day
        "PARTLY_CLOUDY_NIGHT" -> R.mipmap.back_partly_cloud_night
        "WIND" -> R.mipmap.back_clear_day
        "LIGHT_RAIN" -> R.mipmap.back_rain_day
        "MODERATE_RAIN", "HEAVY_RAIN", "STORM_RAIN" -> R.mipmap.back_rain_day
        "THUNDER_SHOWER" -> R.mipmap.back_thunder_shower_day
        "SLEET" -> R.mipmap.back_snow_rain_night
        "LIGHT_SNOW", "MODERATE_SNOW", "HEAVY_SNOW", "STORM_SNOW" -> R.mipmap.back_snow_day_night
        "HAIL" -> R.mipmap.back_rain_day
        "LIGHT_HAZE" -> R.mipmap.back_low_fog_day
        "MODERATE_HAZE" -> R.mipmap.back_mid_fog_day
        "HEAVY_HAZE" -> R.mipmap.back_high_fog_day
        "FOG" -> R.mipmap.back_fog_day
        "DUST" -> R.mipmap.back_high_fog_day
        else -> R.mipmap.back_clear_day
    }
}

fun getSkyBg(isDark:Boolean,sky: String?) = if (isDark) getNightBg(sky) else getDayBg(sky)

fun getNightBg(sky: String?): Int {
    return when (sky) {
        "CLEAR_DAY" -> R.mipmap.back_900n
        "CLOUDY" -> R.mipmap.back_cloudy_night
        "CLEAR_NIGHT" -> R.mipmap.back_clear_night
        "PARTLY_CLOUDY_DAY" -> R.mipmap.back_partly_cloud_night
        "PARTLY_CLOUDY_NIGHT" -> R.mipmap.back_partly_cloud_night
        "WIND" -> R.mipmap.back_clear_night
        "LIGHT_RAIN" -> R.mipmap.back_rain_night
        "MODERATE_RAIN", "HEAVY_RAIN", "STORM_RAIN" -> R.mipmap.back_rain_night
        "THUNDER_SHOWER" -> R.mipmap.back_thunder_shower_night
        "SLEET" -> R.mipmap.back_snow_rain_night
        "LIGHT_SNOW", "MODERATE_SNOW", "HEAVY_SNOW", "STORM_SNOW" -> R.mipmap.back_snow_day_night
        "HAIL" -> R.mipmap.back_rain_night
        "LIGHT_HAZE" -> R.mipmap.back_low_fog_night
        "MODERATE_HAZE" -> R.mipmap.back_mid_fog_night
        "HEAVY_HAZE" -> R.mipmap.back_high_fog_night
        "FOG" -> R.mipmap.back_fog_night
        "DUST" -> R.mipmap.back_high_fog_night
        else -> R.mipmap.back_clear_night
    }
}

fun getSkyName(sky: String?) =
    when (sky) {
        "CLEAR_DAY" -> "晴"
        "CLOUDY" -> "阴"
        "CLEAR_NIGHT" -> "晴"
        "PARTLY_CLOUDY_DAY" -> "多云"
        "PARTLY_CLOUDY_NIGHT" -> "多云"
        "WIND" -> "大风"
        "LIGHT_RAIN" -> "小雨"
        "MODERATE_RAIN" -> "中雨"
        "HEAVY_RAIN" -> "大雨"
        "STORM_RAIN" -> "暴雨"
        "THUNDER_SHOWER" -> "雷阵雨"
        "SLEET" -> "雨夹雪"
        "LIGHT_SNOW" -> "小雪"
        "MODERATE_SNOW" -> "中雪"
        "HEAVY_SNOW" -> "大雪"
        "STORM_SNOW" -> "暴雪"
        "HAIL" -> "冰雹"
        "LIGHT_HAZE" -> "轻度污染"
        "MODERATE_HAZE" -> "中度污染"
        "HEAVY_HAZE" -> "重度污染"
        "FOG" -> "雾"
        "DUST" -> "浮尘"
        else -> "晴"
    }
