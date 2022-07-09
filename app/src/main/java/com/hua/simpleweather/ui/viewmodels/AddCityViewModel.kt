package com.hua.simpleweather.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hua.simpleweather.ActionEvent
import com.hua.simpleweather.db.dao.bean.LocalCity
import com.hua.simpleweather.repository.NetRepository
import com.hua.simpleweather.repository.PlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author : huaweikai
 * @Date   : 2021/12/22
 * @Desc   : viewmodel
 */
@HiltViewModel
class AddCityViewModel @Inject constructor(
    private val placeRepository: PlaceRepository,
    private val netRepository: NetRepository
):ViewModel() {
    //添加地址的事件，用于如果已存在并不添加
    private val _addEvent = MutableStateFlow<ActionEvent>(ActionEvent.Empty)
    val addEvent :StateFlow<ActionEvent> get() = _addEvent

    var cityList = MutableStateFlow<List<LocalCity>>(
        arrayListOf()
    )

    //检索地址
    fun searchPlace(query:String){
        viewModelScope.launch {
            cityList.value = placeRepository.selectCity(query)
        }
    }

    val localCity = placeRepository.selectLocalCity()

    fun addCity(localCity: LocalCity){
        viewModelScope.launch {
            val primary = localCity.lng + localCity.lat
            //用于排序使用
            val num = netRepository.selectNoLocationCount()
            if(netRepository.cityExist(primary) == 0){
                val weather = netRepository.getWeather(localCity.lng,localCity.lat,localCity.cityName,num+1)
                if(weather != null){
                    netRepository.insertWeather(weather)
                    _addEvent.value = ActionEvent.Success
                }else{
                    _addEvent.value = ActionEvent.Error("无网络,添加失败")
                }
            }else{
                _addEvent.value = ActionEvent.Error("已存在")
            }
        }
    }
}


