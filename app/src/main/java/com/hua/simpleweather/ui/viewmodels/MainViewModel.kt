package com.hua.simpleweather.ui.viewmodels

import android.app.Application
import android.content.Context
import android.location.Address
import android.location.LocationManager
import androidx.lifecycle.*
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

    private val _cityListLiveData = MutableLiveData<List<WeatherVO>>()
    val cityListLiveData: LiveData<List<WeatherVO>> get() = _cityListLiveData

    //对本地城市进行检索，因为在城市管理界面需要滑动排序等等，不能直接使用room的流
    fun selectCityList() {
        viewModelScope.launch {
            _cityListLiveData.value = netRepository.selectCityWeather()
        }
    }


    init {
        //启动app刷新一次天气
        viewModelScope.launch {
            if (netRepository.getCityCount() > 0) {
                reFreshWeather(false)
            }
        }
        selectCityList()
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


    /**
     * 刷新分两种，启动app后自动刷新，和手动刷新
     * @param isAction 用于判断是否为人为的刷新
     */
    fun reFreshWeather(isAction: Boolean) {
        if (isAction) {
            _refreshWeather.tryEmit(ActionEvent.Loading)
        }
        viewModelScope.launch {
            val isSuccess = updateWeather(isAction)
            if (isAction) {
                if (isSuccess) _refreshWeather.emit(ActionEvent.Success) else
                    _refreshWeather.emit(ActionEvent.Error("网络不佳"))
            }
        }
    }

    //删除本地城市
    fun deleteWeatherBean(weatherVO: WeatherVO) {
        _cityListLiveData.value = _cityListLiveData.value?.filter { it.id != weatherVO.id }
        //删除后，对本地剩余城市排序，防止id冲突等
        replaceWeatherSort()
    }
    /**
     * 在不是手动刷新的情况下，只刷新时间超过1小时的无效天气，手动刷新，则将所有天气都刷新
     * @param isAction 用于判断是否为人为的刷新
     */
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

    //城市管理界面，滑动后排序手动发送liveData
    fun updateCityIndex(
        fromPosition: Int,
        toPosition: Int,
    ) {
        _cityListLiveData.value?.toMutableList()?.apply {
            //将滑动的先移除
            val remove = removeAt(fromPosition)
            //在将滑动的添加到被加的之前即可
            add(toPosition,remove)
            _cityListLiveData.value = this
            replaceWeatherSort()
        }
    }

    //对本地城市重新排序id，防止之后id混乱
    private fun replaceWeatherSort() {
        viewModelScope.launch {
            netRepository.updateCityCount(_cityListLiveData.value ?: arrayListOf())
        }
    }

    /**
     * @param address 城市信息
     * @return 是否添加成功，为true则证明失败
     */
    suspend fun addLocation(address: Address): Boolean {
        val lng = address.longitude.toString()
        val lat = address.latitude.toString()
        // 从address中获取信息
        val cityName = "${address.locality}${address.featureName}"
        val oldWeather = netRepository.selectLocationWeather()
        //当位置在roundInt后，无变化，其实移动幅度不大，不必更改定位城市
        if (oldWeather != null && oldWeather.lat.toDouble()
                .roundToInt() == address.latitude.roundToInt()
            && oldWeather.lng.toDouble().roundToInt() == address.longitude.roundToInt()
            && System.currentTimeMillis() - oldWeather.updateTime < 1000 * 60 * 60
        ) {
            //在判断后，直接返回false，不再刷新未更新1小时的天气
            return false
        }
        val weather = netRepository.selectWeather(lng, lat) ?: return true
        netRepository.updateWeather(weather.copy(cityName = cityName, id = 0))
        return false
    }

    //获取当前城市数量
    suspend fun currentCityCount():Int{
        return netRepository.getCityCount()
    }
}