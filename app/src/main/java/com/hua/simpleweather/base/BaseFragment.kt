package com.hua.simpleweather.base

import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * @author : huaweikai
 * @Date   : 2021/12/22
 * @Desc   : 基类fragment
 */
open class BaseFragment<T:ViewBinding> :Fragment() {
    private var _bind : T ?= null
    val bind get() = _bind!!

    fun initViewBind(t:T){
        _bind = t
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bind = null
    }
}