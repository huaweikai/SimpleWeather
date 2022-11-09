package com.hua.simpleweather.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hua.model.weather.WeatherVO
import com.hua.simpleweather.R
import com.hua.simpleweather.databinding.FragementWarnBinding
import com.hua.simpleweather.ui.adapter.AlertAdapter


class WarnFragment:BottomSheetDialogFragment() {
    private var _bind :FragementWarnBinding ?= null
    private val bind get() = _bind!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bind = FragementWarnBinding.inflate(layoutInflater)
        return bind.root
    }
    private var alert:WeatherVO.Result.Alert ?= null
    private val adapter = AlertAdapter()

    fun setWarnList(alert: WeatherVO.Result.Alert){
        this.alert = alert
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheet)
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
        if (alert == null) dismiss()
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

    //获取当前屏幕高度
    private fun getPeekHeight(): Int {
        return resources.displayMetrics.heightPixels
    }
}