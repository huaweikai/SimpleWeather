package com.hua.simpleweather.ui.adapter

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import coil.load
import coil.size.Scale
import com.hua.model.weather.WeatherVO
import com.hua.resource.getDayBg
import com.hua.simpleweather.databinding.ItemWeatherPagerBinding
import java.util.*

/**
 * @author : huaweikai
 * @Date   : 2021/12/22
 * @Desc   : adapter
 */

class WeatherViewPagerAdapter(
    private val activity: AppCompatActivity,
    private val attachToWindow:(String)->Unit,
    private val swipRefresh:(SwipeRefreshLayout)->Unit
) : ListAdapter<WeatherVO, WeatherViewPagerAdapter.VHolder>(
    object : DiffUtil.ItemCallback<WeatherVO>() {
        override fun areItemsTheSame(oldItem: WeatherVO, newItem: WeatherVO): Boolean {
            return oldItem.address == newItem.address
        }

        override fun areContentsTheSame(oldItem: WeatherVO, newItem: WeatherVO): Boolean {
            return oldItem == newItem
        }
    }
) {
    inner class VHolder(val bind: ItemWeatherPagerBinding) : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {
        return VHolder(
            ItemWeatherPagerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            this.bind.itemWeatherRefresh.setOnRefreshListener {
                swipRefresh(bind.itemWeatherRefresh)
            }
        }
    }

    override fun onViewAttachedToWindow(holder: VHolder) {
        super.onViewAttachedToWindow(holder)
        //用于对新展示的背景进行取色，设置通知栏颜色
        attachToWindow(
            currentList[holder.adapterPosition].result.realtime.skycon
        )
    }

    override fun onBindViewHolder(holder: VHolder, position: Int) {
        val data = getItem(position)
        val adapter = WeatherModuleAdapter(data,activity)
        //卡片式rv
        holder.bind.itemWeatherRv.apply {
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(holder.itemView.context)
            addItemDecoration(moduleItemDecoration)
        }
        holder.bind.itemWeatherBg.load(
            getDayBg(data.result.realtime.skycon)
        ){
            this.scale(Scale.FIT)
        }
    }
}