package com.hua.simpleweather.di

import android.content.Context
import androidx.room.Room
import com.hua.simpleweather.db.dao.CityDao
import com.hua.simpleweather.db.dao.WeatherDao
import com.hua.simpleweather.db.WeatherRoom
import com.hua.simpleweather.network.interfaces.WeatherService
import com.hua.simpleweather.other.Contacts
import com.hua.simpleweather.other.Contacts.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * @author : huaweikai
 * @Date   : 2021/12/22
 * @Desc   : di
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideOkHttp():OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(5L, TimeUnit.SECONDS)
            .cache(Cache(File("/sdcard/weather"),20*1024*1024))
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit =
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

    @Singleton
    @Provides
    fun provideWeatherAPI(
        retrofit: Retrofit
    ): WeatherService = retrofit.create(WeatherService::class.java)



    @Provides
    @Singleton
    fun provideWeatherRoom(
        @ApplicationContext context: Context
    ): WeatherRoom =Room.databaseBuilder(
        context,
        WeatherRoom::class.java,
        Contacts.ROOM_NAME
    ).build()

    @Provides
    @Singleton
    fun provideWeatherDao(
        room: WeatherRoom
    ):WeatherDao = room.weatherDao

    @Provides
    @Singleton
    fun provideCityDao(
        room: WeatherRoom
    ):CityDao = room.cityDao
}