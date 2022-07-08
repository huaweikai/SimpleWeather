package com.hua.simpleweather.di

import android.content.Context
import androidx.room.Room
import com.hua.network.Contacts
import com.hua.simpleweather.db.dao.CityDao
import com.hua.simpleweather.db.dao.WeatherDao
import com.hua.simpleweather.db.WeatherRoom
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @author : huaweikai
 * @Date   : 2021/12/22
 * @Desc   : di
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {


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