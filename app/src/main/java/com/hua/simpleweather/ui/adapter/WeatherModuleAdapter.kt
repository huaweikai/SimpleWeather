package com.hua.simpleweather.ui.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.hua.material.materialcolor.PaletteUtils
import com.hua.model.weather.WeatherVO
import com.hua.resource.getSkyBg
import com.hua.simpleweather.MainActivity
import com.hua.simpleweather.R
import com.hua.simpleweather.databinding.*
import com.hua.simpleweather.ui.adapter.holder.*
import com.hua.simpleweather.ui.fragments.WarnFragment
import com.hua.simpleweather.ui.showMorePopWindow
import com.hua.simpleweather.utils.isDarkMode


class WeatherModuleAdapter(
    private val data: WeatherVO,
    private val activity: AppCompatActivity
) : RecyclerView.Adapter<AbstractMainHolder>() {

    //这个才是用于排序卡片式的
    private val typeList = arrayListOf(
        ViewType.ViewTypeNow, ViewType.ViewTypeRealTime,
        ViewType.ViewTypeHour,ViewType.ViewTypeDaily, ViewType.ViewTypeSun,
        ViewType.ViewTypeAirDetail,
        ViewType.ViewTypeDetail,
        ViewType.ViewTypeAbout
    )

    //返回对应的 type值
    override fun getItemViewType(position: Int): Int {
        return typeList[position].type
    }

    private val isDarkMode = activity.applicationContext.isDarkMode

    init {
        //当预警不为空就把预警卡片添加进去
        if (data.result.alert.content.isNotEmpty()) {
            typeList.add(1, ViewType.ViewTypeWarn)
        } else {
            //当为空，且存在就移除，如果不存在，不做处理，因为本来它就不是默认存在的
            if (typeList.contains(ViewType.ViewTypeWarn)) {
                typeList.removeAt(1)
            }
        }
    }

//    override fun onViewAttachedToWindow(holder: AbstractMainHolder) {
//        super.onViewAttachedToWindow(holder)
//        val a: Animator = ObjectAnimator.ofFloat(holder.itemView, "alpha", 0f, 1f)
//        a.duration = 300
//        a.startDelay = 100
//        a.interpolator = FastOutSlowInInterpolator()
//        a.start()
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractMainHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        //对背景进行取色，用于设置卡片的标题等等
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
        //实例化对应的卡片Holder
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
                    bind.itemNowMore.setOnClickListener {
                        it.showMorePopWindow(activity.findNavController(R.id.fragmentContainerView),data)
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
            ViewType.ViewTypeDaily.type ->{
                DailyDayHolder(ItemDailyCardBinding.inflate(layoutInflater,parent,false),color)
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
        //绑定对应holder的数据
        holder.onBindView(data)
    }
    //当前卡片数量
    override fun getItemCount() = typeList.size
}

//对图片进行压缩
fun decodeBitmap(@AttrRes id: Int, context: Context): Bitmap {
    val bitmapOption = BitmapFactory.Options()
    bitmapOption.inSampleSize = 2000
    bitmapOption.inPreferredConfig = Bitmap.Config.RGB_565
    return BitmapFactory.decodeResource(context.resources, id, bitmapOption)
}

/**
 * @param type 只是用于判断id，并不是排序依据
 */
sealed class ViewType(val type: Int) {
    object ViewTypeNow : ViewType(1)
    object ViewTypeWarn : ViewType(2)
    object ViewTypeHour : ViewType(3)
    object ViewTypeRealTime : ViewType(4)
    object ViewTypeDaily:ViewType(5)
    object ViewTypeSun : ViewType(6)
    object ViewTypeAirDetail:ViewType(7)
    object ViewTypeDetail:ViewType(8)
    object ViewTypeAbout : ViewType(9)
}