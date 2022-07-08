package com.hua.simpleweather.db.dao.bean

/**
 * @author : huaweikai
 * @Date   : 2021/12/23
 * @Desc   : ben
 */
data class CityBean(
    val id:Int,
    val lng:String,
    val lat:String,
    val cityName:String,
    val updateTime:Long
)