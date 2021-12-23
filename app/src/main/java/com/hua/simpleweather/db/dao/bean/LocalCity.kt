package com.hua.simpleweather.db.dao.bean

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author : huaweikai
 * @Date   : 2021/12/22
 * @Desc   : city
 */
@Entity
data class LocalCity (
    @PrimaryKey
    val code:String,
    val cityName:String,
    val lng:String,
    val lat:String
)