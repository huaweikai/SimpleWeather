package com.hua.simpleweather.ui.fragments

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hua.model.weather.WeatherVO
import com.hua.simpleweather.databinding.FragementWarnBinding
import com.hua.simpleweather.ui.adapter.AlertAdapter
import com.hua.simpleweather.utils.dp
import com.hua.simpleweather.utils.isDarkMode
import com.hua.simpleweather.utils.windowSize


class WarnFragment:BottomSheetDialogFragment() {
    private var _bind :FragementWarnBinding ?= null
    private val bind get() = _bind!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bind = FragementWarnBinding.inflate(layoutInflater)
        return bind.root.apply {
            setBackgroundColor( if (context?.isDarkMode() == true) Color.BLACK else Color.WHITE)
        }
    }
    private var alert:WeatherVO.Result.Alert ?= null
    private val adapter = AlertAdapter()

    fun setWarnList(alert: WeatherVO.Result.Alert){
        this.alert = alert
    }


    override fun onStart() {
        super.onStart()

        val layoutParams = bind.warnGroup.layoutParams
        layoutParams.height = getPeekHeight()
        bind.warnGroup.layoutParams = layoutParams
        (dialog as? BottomSheetDialog)?.behavior?.apply {
            skipCollapsed = true
            state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.submitList(alert?.content)
        bind.warnRv.adapter = adapter
        bind.materialToolbar.setNavigationOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bind = null
    }

    private fun getPeekHeight(): Int{
        val height = resources.displayMetrics.heightPixels
        return height
    }
}