package com.hua.simpleweather.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hua.simpleweather.ActionEvent.*
import com.hua.simpleweather.base.BaseFragment
import com.hua.simpleweather.databinding.FragmentAddCityBinding
import com.hua.simpleweather.ui.adapter.AddCityAdapter
import com.hua.simpleweather.ui.viewmodels.AddCityViewModel
import com.hua.simpleweather.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AddCityFragment : BaseFragment<FragmentAddCityBinding>() {

    private val viewModel by viewModels<AddCityViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initViewBind(FragmentAddCityBinding.inflate(layoutInflater))
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = AddCityAdapter{
            viewModel.addCity(it)
        }
        val watch = object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.searchPlace(s.toString())
            }
        }
        bind.addcityEd.addTextChangedListener(watch)

        bind.addrv.apply {
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(requireContext())
        }


        lifecycleScope.launch {
            viewModel.cityList.collect{
                adapter.submitList(it)
            }
        }

        lifecycleScope.launch {
            viewModel.localCity.collect {
                adapter.updateLocalList(it)
            }
        }

        lifecycleScope.launch {
            viewModel.addEvent.collect {
                when(it){
                    is Error-> it.message.toast(requireContext())
                    is Success -> "添加成功".toast(requireContext())
                    else->{}
                }
            }
        }
    }
}