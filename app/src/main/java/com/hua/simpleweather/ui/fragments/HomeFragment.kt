package com.hua.simpleweather.ui.fragments

import android.graphics.BitmapFactory
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.palette.graphics.Palette
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hua.network.Contacts.CITY_TO_HOME
import com.hua.resource.getDayBg
import com.hua.resource.getSkyBg
import com.hua.simpleweather.ActionEvent
import com.hua.simpleweather.R
import com.hua.simpleweather.base.BaseFragment
import com.hua.simpleweather.databinding.FragmentHomeBinding
import com.hua.simpleweather.ui.adapter.WeatherViewPagerAdapter
import com.hua.simpleweather.ui.viewmodels.MainViewModel
import com.hua.simpleweather.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val viewModel by activityViewModels<MainViewModel>()
    private var swip: SwipeRefreshLayout? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initViewBind(FragmentHomeBinding.inflate(layoutInflater))
        viewModel.selectCityList()
        return bind.root
    }

    private val homeHandler = HomeHandler(this)

    class HomeHandler(fragment: Fragment) : Handler(Looper.getMainLooper()) {
        private var weakFragment: WeakReference<Fragment>? = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        lifecycleScope.launch {
            viewModel.refreshWeather.collect {
                when (it) {
                    is ActionEvent.Loading -> swip?.isRefreshing = true
                    is ActionEvent.Error -> {
                        swip?.isRefreshing = false
                        swip = null
                        it.message.toast(requireContext())
                    }
                    is ActionEvent.Success -> {
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

    private fun initView() {
        var isFirst = true
        val position = arguments?.getInt(CITY_TO_HOME, 0) ?: 0
        val adapter =
            WeatherViewPagerAdapter(requireActivity() as AppCompatActivity, attachToWindow = {
                //延时半秒，如果一直滑动就会导致界面卡顿，先remove在添加，如果小于半秒，则不会执行post的代码
                homeHandler.removeCallbacksAndMessages(null)
                homeHandler.postDelayed({
                    setSystemStatus(
                        getSkyBg(requireActivity().isDarkMode(), it)
                    )
                },500L)
            }, swipRefresh = {
                swip = it
                viewModel.reFreshWeather(true)
            })

        bind.homeViewpager.adapter = adapter

        lifecycleScope.launch {
            viewModel.roomWeather.collect {
                adapter.submitList(it)
                if (isFirst) {
//                如果是第一次就设置，并在设置后，将first设置为false
                    val index= if(position != -1){
                        position
                    }else {
                        it.size - 1
                    }
                    bind.homeViewpager.setCurrentItem(index, false)
                    isFirst = false
                }
            }
        }
    }

    private fun setSystemStatus(res: Int) {
        val bitmap = BitmapFactory.decodeResource(resources, res)
        val frame = Rect()
        requireActivity().window.decorView.getWindowVisibleDisplayFrame(frame)
        val statusBarHeight: Int = frame.top
        Palette.from(bitmap)
            .setRegion(
                0,
                0,
                resources.configuration.screenWidthDp.dpToPx(requireContext()).toInt(),
                statusBarHeight.dp
            )
            .generate {
                it?.let { palette ->
                    var mostPopularSwatch: Palette.Swatch? = null
                    for (swatch in palette.swatches) {
                        if (mostPopularSwatch == null || swatch.population > mostPopularSwatch.population) {
                            mostPopularSwatch = swatch
                        }
                    }
                    mostPopularSwatch?.let { swatch ->
                        val luminance = ColorUtils.calculateLuminance(swatch.rgb)
                        requireActivity().setLightStatusBar(luminance > 0.5)
                    }
                }
                bitmap.recycle()
            }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        homeHandler.removeCallbacksAndMessages(null)
        //退出界面后，恢复默认的通知栏颜色
        requireActivity().setLightStatusBar(resources.configuration.uiMode == 0x11)
    }
}