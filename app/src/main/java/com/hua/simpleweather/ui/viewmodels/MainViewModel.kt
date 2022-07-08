package com.hua.simpleweather.ui.viewmodels

import android.app.Application
import android.content.Context
import android.location.Address
import android.location.LocationManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hua.simpleweather.ActionEvent
import com.hua.simpleweather.db.dao.bean.LocalCity
import com.hua.network.Contacts.FIRST_ACTION
import com.hua.network.Contacts.FIRST_ACTION_IS_FIRST
import com.hua.model.weather.WeatherVO
import com.hua.simpleweather.repository.NetRepository
import com.hua.simpleweather.repository.PlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject
import kotlin.math.roundToInt

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
    private val _refreshWeather = MutableSharedFlow<ActionEvent>()
    val refreshWeather: SharedFlow<ActionEvent> get() = _refreshWeather

    val roomWeather = netRepository.getAllWeather()

    val roomWeatherLiveData = roomWeather.asLiveData(viewModelScope.coroutineContext).value

    var cityList = listOf<WeatherVO>()

    fun selectCityList(
        updateView: () -> Unit
    ) {
        viewModelScope.launch {
            cityList = netRepository.selectCityWeather()
            updateView()
        }
    }


    init {
        //启动app刷新一次天气
        viewModelScope.launch {
            if (netRepository.getCityCount() > 0) {
                reFreshWeather(false)
            }
        }
    }

    //第一次进入软件进行地址导入
    fun firstAction() {
        val inputSteam = InputStreamReader(getApplication<Application>().assets.open("city.csv"))
        inputSteam.use {
            val reader = BufferedReader(it)
            reader.forEachLine { city ->
                //还是拿经纬度直接当主键，后续有用
                val data = city.split(",")
                val cityName = data[1]
                val lng = data[2]
                val lat = data[3]
                viewModelScope.launch(Dispatchers.IO) {
                    placeRepository.insertCity(LocalCity(cityName, lng, lat))
                }
            }
        }
        getApplication<Application>().getSharedPreferences(FIRST_ACTION, Context.MODE_PRIVATE)
            .edit().putBoolean(
                FIRST_ACTION_IS_FIRST, false
            ).apply()
    }


    //批量刷新天气
    fun reFreshWeather(isAction:Boolean) {
        if(isAction){
            _refreshWeather.tryEmit(ActionEvent.Loading)
        }
        viewModelScope.launch {
            val isSuccess = updateWeather(isAction)
            if(isAction){
                if(isSuccess) _refreshWeather.emit(ActionEvent.Success) else
                    _refreshWeather.emit(ActionEvent.Error("网络不佳"))

            }
        }
    }

    //删除本地城市
    fun deleteWeatherBean(weatherPO: WeatherVO) {
        viewModelScope.launch {
            netRepository.deleteCity(weatherPO)
            //当删除非最后一个城市后，里面的id可能会错乱，导致再次添加城市时会出现两个id一样的情况
            //所以在删除之后再次刷新天气时可以做到顺序重排
            //但此时刷新要做到的是用户无感知，所以将刷新天气提取出来，不需要提示用户
            updateWeather(false)
            selectCityList { }
        }
    }

    private suspend fun updateWeather(isAction: Boolean): Boolean {
        var isSuccess = false
        placeRepository.selectCities().forEachIndexed { index, it ->
            if (it.id == 0 && !isAction) {
                return@forEachIndexed
            }
            if (isAction || System.currentTimeMillis() - it.updateTime > 1000 * 60 * 60) {
                val weather = netRepository.getWeather(it.lng, it.lat, it.cityName, index)
                netRepository.insertWeather(weather)
            }
            isSuccess = true
        }
        return isSuccess
    }

    fun updateCity(
        fromPosition: Int,
        toPosition: Int,
        replaceOk: (List<WeatherVO>) -> Unit
    ) {
        cityList.toMutableList().apply {
            removeAt(fromPosition)
            add(toPosition, cityList[fromPosition])
            replaceOk(this)
            cityList = this
        }
    }


    fun replaceWeatherSort() {
        viewModelScope.launch {
            netRepository.updateCityCount(cityList)
        }
    }

    suspend fun addLocation(address: Address): Boolean {
        val lng = address.longitude.toString()
        val lat = address.latitude.toString()
        val cityName = "${address.locality}${address.featureName}"
        val oldWeather = netRepository.selectLocationWeather()
        if (oldWeather!=null && oldWeather.lat.toDouble().roundToInt() == address.latitude.roundToInt()
            && oldWeather.lng.toDouble().roundToInt() == address.longitude.roundToInt()
            && System.currentTimeMillis() - oldWeather.updateTime < 1000 * 60 * 60) {
            return false
        }
        val weather = netRepository.selectWeather(lng, lat) ?: return true
        netRepository.updateWeather(weather.copy(cityName = cityName, id = 0))
        return false
    }
}