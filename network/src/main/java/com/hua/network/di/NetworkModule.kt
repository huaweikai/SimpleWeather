package com.hua.network.di

import com.hua.network.Contacts
import com.hua.network.api.WeatherService
import com.hua.network.calladapter.ApiResultCallAdapterFactory
import com.hua.network.converter.asConverterFactory
import com.hua.network.interceptor.BusinessErrorInterceptor
import com.hua.network.utils.globalJson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * @author : huaweikai
 * @Date   : 2022/04/09
 * @Desc   :
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideOkhttp() = OkHttpClient.Builder()
        .addInterceptor(BusinessErrorInterceptor())
        .callTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .addCallAdapterFactory(ApiResultCallAdapterFactory())
        .addConverterFactory(globalJson.asConverterFactory("application/json".toMediaType()))
        .baseUrl(Contacts.BASE_URL)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideNetService(
        retrofit: Retrofit
    ) = retrofit.create<WeatherService>()
}