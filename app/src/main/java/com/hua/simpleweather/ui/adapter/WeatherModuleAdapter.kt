package com.hua.simpleweather.ui.adapter

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.hua.material.materialcolor.ColorContainerData
import com.hua.material.materialcolor.PaletteUtils
import com.hua.model.weather.WeatherVO
import com.hua.resource.getSkyBg
import com.hua.resource.getSkyName
import com.hua.simpleweather.MainActivity
import com.hua.simpleweather.R
import com.hua.simpleweather.databinding.*
import com.hua.simpleweather.db.dao.bean.WeatherPO
import com.hua.simpleweather.ui.adapter.holder.*
import com.hua.simpleweather.utils.isDarkMode
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream


class WeatherModuleAdapter(
    private val data: WeatherVO,
    private val activity: AppCompatActivity
) : RecyclerView.Adapter<AbstractMainHolder>() {

    private val typeList = listOf(
        ViewTye.ViewTypeNow, ViewTye.ViewTypeRealTime,
        ViewTye.ViewTypeHour, ViewTye.ViewTypeSun,
        ViewTye.ViewTypeAbout
    )

    override fun getItemViewType(position: Int): Int {
        return typeList[position].type
    }

    private val isDarkMode = activity.applicationContext.isDarkMode()

    override fun onViewAttachedToWindow(holder: AbstractMainHolder) {
        super.onViewAttachedToWindow(holder)
        val a: Animator = ObjectAnimator.ofFloat(holder.itemView, "alpha", 0f, 1f)
        a.duration = 300
        a.startDelay = 100
        a.interpolator = FastOutSlowInInterpolator()
        a.start()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractMainHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ViewTye.ViewTypeNow.type -> {
                NowHolder(
                    ItemNowBinding.inflate(
                        layoutInflater, parent, false
                    )
                ).apply {
                    bind.itemNowAdd.setOnClickListener {
                        (activity as MainActivity).navController.navigate(R.id.cityFragment)
                    }
                }
            }
            ViewTye.ViewTypeRealTime.type -> {
                DayKeypointHolder(
                    ItemRealtimeSurveyCardBinding.inflate(
                        layoutInflater,
                        parent,
                        false
                    )
                )
            }
            ViewTye.ViewTypeHour.type -> {
                HourHolder(ItemHourCardBinding.inflate(layoutInflater, parent, false))
            }
            ViewTye.ViewTypeSun.type -> {
                AstroHolder(ItemAtrsoCardBinding.inflate(layoutInflater, parent, false))
            }
            else -> {
                AboutHolder(
                    ItemAboutBinding.inflate(
                        layoutInflater, parent, false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: AbstractMainHolder, position: Int) {
        val color = PaletteUtils.resolveByBitmap(
            decodeBitmap(
                getSkyBg(isDarkMode, data.result.realtime.skycon),
                activity
            ), isDarkMode
        )
        holder.onBindView(
            data,
            colorData = color.copy(
                containerColor = ColorUtils.setAlphaComponent(
                    color.primaryColor,
                    (0.5 * 255).toInt()
                )
            )
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

sealed class ViewTye(val type: Int) {
    object ViewTypeNow : ViewTye(1)
    object ViewTypeRealTime : ViewTye(2)
    object ViewTypeHour : ViewTye(3)
    object ViewTypeSun : ViewTye(4)
    object ViewTypeAbout : ViewTye(6)
}