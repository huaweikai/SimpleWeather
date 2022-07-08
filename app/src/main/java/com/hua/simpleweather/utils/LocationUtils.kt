package com.hua.simpleweather.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.*
import android.os.Build
import android.os.CancellationSignal
import android.os.Looper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.*

object LocationUtils {
    //获取地址信息:城市、街道等信息
    fun getAddress(
        context: Context,
        lng:Double,
        lat:Double
    ): Address? {
        var result: List<Address?>? = null
        val gc = Geocoder(context, Locale.getDefault())
        result = gc.getFromLocation(
            lat,
            lng, 1
        )
        return if(result.isEmpty()) null else result[0]
    }
}

@SuppressLint("MissingPermission")
fun LocationManager.getLocation(
    context: Context,
    provider: String,
    getAddress:(Address?)->Unit
){
    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
        getCurrentLocation(provider, CancellationSignal(),context.mainExecutor){it->
            if(it != null){
                getAddress(LocationUtils.getAddress(context,it.longitude,it.latitude))
            }else{
                getAddress(null)
            }

        }

    }else{
        requestSingleUpdate(
            provider,
            { it ->
                getAddress(LocationUtils.getAddress(context,it.longitude,it.latitude))
            }, Looper.getMainLooper()
        )
    }
}
