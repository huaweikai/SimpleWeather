package com.hua.simpleweather.ui.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.RecyclerView
import com.hua.material.materialcolor.PaletteUtils
import com.hua.model.weather.WeatherVO
import com.hua.resource.getSkyBg
import com.hua.simpleweather.MainActivity
import com.hua.simpleweather.R
import com.hua.simpleweather.databinding.*
import com.hua.simpleweather.ui.adapter.holder.*
import com.hua.simpleweather.ui.fragments.WarnFragment
import com.hua.simpleweather.utils.isDarkMode


class WeatherModuleAdapter(
    private val data: WeatherVO,
    private val activity: AppCompatActivity
) : RecyclerView.Adapter<AbstractMainHolder>() {

    private val typeList = arrayListOf(
        ViewType.ViewTypeNow, ViewType.ViewTypeRealTime,
        ViewType.ViewTypeHour, ViewType.ViewTypeSun,
        ViewType.ViewTypeAirDetail,
        ViewType.ViewTypeDetail,
        ViewType.ViewTypeAbout
    )

    override fun getItemViewType(position: Int): Int {
        return typeList[position].type
    }

    private val isDarkMode = activity.applicationContext.isDarkMode()

    init {
        if (data.result.alert.content.isNotEmpty()) {
            typeList.add(1, ViewType.ViewTypeWarn)
        } else {
            if (typeList.contains(ViewType.ViewTypeWarn)) {
                typeList.removeAt(1)
            }
        }
    }

    override fun onViewAttachedToWindow(holder: AbstractMainHolder) {
        super.onViewAttachedToWindow(holder)
//        val a: Animator = ObjectAnimator.ofFloat(holder.itemView, "alpha", 0f, 1f)
//        a.duration = 300
//        a.startDelay = 100
//        a.interpolator = FastOutSlowInInterpolator()
//        a.start()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractMainHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val color = PaletteUtils.resolveByBitmap(
            decodeBitmap(
                getSkyBg(isDarkMode, data.result.realtime.skycon),
                activity
            ), isDarkMode
        ).let {
            it.copy(
                containerColor = ColorUtils.setAlphaComponent(
                    it.primaryColor,
                    (0.5 * 255).toInt()
                )
            )
        }
        return when (viewType) {
            ViewType.ViewTypeNow.type -> {
                NowHolder(
                    ItemNowCardBinding.inflate(
                        layoutInflater, parent, false
                    ),color
                ).apply {
                    bind.itemNowAdd.setOnClickListener {
                        (activity as MainActivity).navController.navigate(R.id.action_homeFragment_to_cityFragment)
                    }
                }
            }
            ViewType.ViewTypeRealTime.type -> {
                DayKeypointHolder(
                    ItemRealtimeSurveyCardBinding.inflate(
                        layoutInflater,
                        parent,
                        false
                    ),color
                )
            }
            ViewType.ViewTypeHour.type -> {
                HourHolder(ItemHourCardBinding.inflate(layoutInflater, parent, false),color)
            }
            ViewType.ViewTypeSun.type -> {
                AstroHolder(ItemAtrsoCardBinding.inflate(layoutInflater, parent, false),color)
            }
            ViewType.ViewTypeWarn.type -> {
                WarnHolder(ItemWarningCardBinding.inflate(layoutInflater, parent, false),color).apply {
                    itemView.setOnClickListener {
                        activity.supportFragmentManager.also {
                            val fragment = (it.findFragmentByTag(WarnFragment::class.simpleName)
                                ?: WarnFragment()) as WarnFragment
                            fragment.setWarnList(data.result.alert)
                            fragment.show(it,WarnFragment::class.simpleName)
                        }
                    }
                }
            }
            ViewType.ViewTypeDetail.type->{
                DetailHolder(ItemRealtimeDetailCardBinding.inflate(layoutInflater,parent,false),color)
            }
            ViewType.ViewTypeAirDetail.type->{
                AirHolder(ItemAirCardBinding.inflate(layoutInflater,parent,false),color)
            }
            else -> {
                AboutHolder(
                    ItemAboutBinding.inflate(
                        layoutInflater, parent, false
                    ),color
                )
            }
        }
    }

    override fun onBindViewHolder(holder: AbstractMainHolder, position: Int) {
        holder.onBindView(
            data,
//            colorData = ColorContainerData(Color.RED,Color.CYAN)
//            colorData = color.copy(
//                containerColor = ColorUtils.setAlphaComponent(
//                    color.primaryColor,
//                    (0.5 * 255).toInt()
//                )
//            )
        )
//        val colorData =
//        data.let {
//
//        }
    }

    override fun getItemCount() = typeList.size
}

fun decodeBitmap(@AttrRes id: Int, context: Context): Bitmap {
    val bitmapOption = BitmapFactory.Options()
    bitmapOption.inSampleSize = 2000
    bitmapOption.inPreferredConfig = Bitmap.Config.RGB_565
    return BitmapFactory.decodeResource(context.resources, id, bitmapOption)
}

sealed class ViewType(val type: Int) {
    object ViewTypeNow : ViewType(1)
    object ViewTypeWarn : ViewType(2)
    object ViewTypeRealTime : ViewType(3)
    object ViewTypeHour : ViewType(4)
    object ViewTypeSun : ViewType(5)
    object ViewTypeAirDetail:ViewType(6)
    object ViewTypeDetail:ViewType(7)
    object ViewTypeAbout : ViewType(8)
}