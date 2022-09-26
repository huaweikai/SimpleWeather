package com.hua.simpleweather.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.*
import android.os.Build
import android.os.CancellationSignal
import android.os.Looper
import java.util.*

object LocationUtils {
    //获取地址信息:城市、街道等信息
    fun getAddress(
        context: Context,
        lng:Double,
        lat:Double
    ): Address? {
        val result: List<Address?>?
        val gc = Geocoder(context, Locale.getDefault())
        result = gc.getFromLocation(
            lat,
            lng, 1
        )
        return if(result.isNullOrEmpty()) null else result[0]
    }
}

@SuppressLint("MissingPermission")
fun LocationManager.getLocation(
    context: Context,
    provider: String,
    getAddress:(Address?)->Unit
){
    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
        getCurrentLocation(provider, CancellationSignal(),context.mainExecutor){
            if(it != null){
                getAddress(LocationUtils.getAddress(context,it.longitude,it.latitude))
            }else{
                getAddress(null)
            }

        }

    }else{
        requestSingleUpdate(
            provider,
            {
                getAddress(LocationUtils.getAddress(context,it.longitude,it.latitude))
            }, Looper.getMainLooper()
        )
    }
}
