@file:Suppress("unused")
package com.hua.simpleweather.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.hua.simpleweather.ActionEvent
import com.hua.simpleweather.db.dao.bean.LocalCity
import com.hua.simpleweather.repository.NetRepository
import com.hua.simpleweather.repository.PlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
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

    //用于判断那些城市已经添加进去了
    val localCity = placeRepository.selectLocalCity()

    private val pagingConfig = PagingConfig(
        pageSize = 50,
        enablePlaceholders = true,
        prefetchDistance = 0,
        initialLoadSize = 50,
        maxSize = 200
    )

    var key = ""

    val pagingSelectPaging = Pager(pagingConfig){
        placeRepository.queryCities(key)
    }.flow

    //添加城市
    fun addCity(localCity: LocalCity){
        viewModelScope.launch {
            val primary = localCity.lng + localCity.lat
            //检索非定位城市有多少，进行id计算
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


