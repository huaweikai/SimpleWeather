package com.hua.simpleweather.ui.adapter.holder

import android.text.SpannableStringBuilder
import android.text.SpannedString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.TextAppearanceSpan
import androidx.core.text.set
import com.hua.material.materialcolor.ColorContainerData
import com.hua.model.weather.WeatherVO
import com.hua.resource.getWarnColor
import com.hua.resource.getWarnDec
import com.hua.resource.getWarnIcon
import com.hua.simpleweather.R
import com.hua.simpleweather.databinding.ItemWarningCardBinding
import com.hua.simpleweather.utils.getString
import com.hua.simpleweather.utils.valueToPx


class WarnHolder(
    private val bind:ItemWarningCardBinding,
    colorData: ColorContainerData
) :AbstractMainHolder(bind,colorData){
    override fun onBindView(data: WeatherVO) {
        bind.apply {
            itemWarnTitle.setTextColor(colorData.primaryColor)
            val code = data.result.alert.content[0].code
            itemWarnIcon.setColorFilter(getWarnColor(code))
            itemWarnIcon.setImageResource(getWarnIcon(code))
            val desc = data.result.alert.content[0].description
            val level = "${getWarnDec(code)}警告:"
            val textSpan = SpannableStringBuilder("$level$desc")
            textSpan[level.length, textSpan.length] = AbsoluteSizeSpan(12.valueToPx(itemView.context).toInt())
            textSpan[level.length, textSpan.length] = ForegroundColorSpan(colorData.containerColor)
            itemWarnDesc.text = textSpan
        }
    }
}