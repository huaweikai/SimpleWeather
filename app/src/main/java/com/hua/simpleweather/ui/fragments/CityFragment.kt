package com.hua.simpleweather.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.hua.simpleweather.R
import com.hua.simpleweather.base.BaseFragment
import com.hua.simpleweather.databinding.FragmentCityBinding
import com.hua.simpleweather.other.Contacts.CITY_TO_HOME
import com.hua.simpleweather.ui.adapter.CityAdapter
import com.hua.simpleweather.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class CityFragment : BaseFragment<FragmentCityBinding>() {

    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initViewBind(FragmentCityBinding.inflate(layoutInflater))
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind.citySearch.setOnClickListener {
            //虚假的搜索按钮
            findNavController().navigate(R.id.action_cityFragment_to_addCityFragment)
        }
        val adapter = CityAdapter(
            {
                //点到了删除按钮
                viewModel.deleteWeatherBean(it)
            },
            { position ->
                //点到item，回到天气界面
                Bundle().also {
                    it.putInt(CITY_TO_HOME, position)
                    findNavController().navigate(R.id.action_cityFragment_to_homeFragment, it)
                }
            },
            {
                //如果adapter某一个长按，将确认键显示
                bind.cityOk.isVisible = true
            }
        )

        bind.cityRv.apply {
            this.adapter = adapter
            this.layoutManager = GridLayoutManager(requireContext(), 2)
        }

        lifecycleScope.launch {
            viewModel.roomWeather.collect {
                adapter.submitList(it)
            }
        }
        bind.cityOk.setOnClickListener {
            //如果删除完毕，再次更新adapter，让删除图标消失,同时让确认图标消失
            adapter.isDeleteTime = false
            adapter.notifyItemRangeChanged(0, adapter.itemCount)
            bind.cityOk.isVisible = false
        }
    }

}