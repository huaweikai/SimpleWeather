package com.hua.simpleweather.ui.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hua.simpleweather.ActionEvent
import com.hua.simpleweather.db.dao.bean.LocalCity
import com.hua.simpleweather.db.dao.bean.WeatherBean
import com.hua.simpleweather.network.bean.Weather
import com.hua.simpleweather.other.Contacts.FIRST_ACTION
import com.hua.simpleweather.other.Contacts.FIRST_ACTION_ISFIRST
import com.hua.simpleweather.repository.NetRepository
import com.hua.simpleweather.repository.PlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

/**
 * @author : huaweikai
 * @Date   : 2021/12/22
 * @Desc   : mainViewModel
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val netRepository: NetRepository,
    private val placeRepository: PlaceRepository,
    context: Application
) : AndroidViewModel(context) {

    //刷新天气的事件，用于刷新后将swiplayout停止
    private val _refreshWeather = MutableStateFlow<ActionEvent>(ActionEvent.Empty)
    val refreshWeather :StateFlow<ActionEvent> get() = _refreshWeather

    val roomWeather = netRepository.getAllWeather()

    //第一次进入软件进行地址导入
    fun firstAction() {
        val inputSteam = InputStreamReader(getApplication<Application>().assets.open("city.csv"))
        inputSteam.use {
            val reader = BufferedReader(it)
            reader.forEachLine { city ->
                val data = city.split(",")
                val code = data[0]
                val cityName = data[1]
                val lng = data[2]
                val lat = data[3]
                LocalCity(code, cityName, lng, lat).also {
                    viewModelScope.launch(Dispatchers.IO) {
                        placeRepository.insertCity(it)
                    }
                }
            }
        }
        getApplication<Application>().getSharedPreferences(FIRST_ACTION, Context.MODE_PRIVATE)
            .edit().putBoolean(
                FIRST_ACTION_ISFIRST, false
            ).apply()
    }


    //批量刷新天气
    fun reFreshWeather(){
        _refreshWeather.value = ActionEvent.Loading
        viewModelScope.launch{
            placeRepository.selectCities().forEach {
                    netRepository.getWeather(it.lng,it.lat,it.cityName)?.let { weather->
                        netRepository.insertWeather(weather)
                }
            }
            _refreshWeather.value = ActionEvent.Success
        }
    }

    //删除本地城市
    fun deleteWeatherBean(weatherBean: WeatherBean){
        viewModelScope.launch {
            netRepository.deleteCity(weatherBean)
        }
    }
}