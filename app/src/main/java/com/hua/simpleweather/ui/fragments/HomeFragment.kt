package com.hua.simpleweather.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hua.simpleweather.ActionEvent
import com.hua.simpleweather.R
import com.hua.simpleweather.base.BaseFragment
import com.hua.simpleweather.db.dao.WeatherDao
import com.hua.simpleweather.databinding.FragmentHomeBinding
import com.hua.simpleweather.network.interfaces.WeatherService
import com.hua.simpleweather.other.Contacts.CITY_TO_HOME
import com.hua.simpleweather.ui.adapter.WeatherViewPagerAdapter
import com.hua.simpleweather.ui.viewmodels.MainViewModel
import com.hua.simpleweather.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val viewModel by activityViewModels<MainViewModel>()
    private var swip:SwipeRefreshLayout?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initViewBind(FragmentHomeBinding.inflate(layoutInflater))
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var isFirst = true
        val position = arguments?.getInt(CITY_TO_HOME,0) ?: 0
        val adapter = WeatherViewPagerAdapter(requireContext(),
            {
                //直接去城市管理
                findNavController().navigate(R.id.cityFragment)
            },{
                //下拉刷新
                //将刷新的控件id传递，去viewmodel调用刷新
                swip = it
                viewModel.reFreshWeather()
            }
        )
        bind.homeViewpager.adapter = adapter
        lifecycleScope.launch {
            viewModel.roomWeather.collect {
                adapter.submitList(it)
                if(isFirst){
                    //如果是第一次就设置，并在设置后，将first设置为false
                    bind.homeViewpager.setCurrentItem(position,false)
                    isFirst = false
                }
            }
        }
        lifecycleScope.launch {
            viewModel.refreshWeather.collect {
                when(it){
                    is ActionEvent.Loading -> swip?.isRefreshing = true
                    is ActionEvent.Error -> {
                        swip?.isRefreshing = false
                        swip = null
                        it.message.toast(requireContext())
                    }
                    is ActionEvent.Success->{
                        swip?.isRefreshing = false
                        swip = null
                        "刷新成功".toast(requireContext())
                    }
                    else -> {
                        swip?.isRefreshing = false
                        swip = null
                    }
                }
            }
        }
    }
}