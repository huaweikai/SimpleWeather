package com.hua.simpleweather.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.hua.simpleweather.ActionEvent
import com.hua.simpleweather.R
import com.hua.simpleweather.base.BaseFragment
import com.hua.simpleweather.databinding.FragmentAddCityBinding
import com.hua.simpleweather.ui.adapter.AddCityAdapter
import com.hua.simpleweather.ui.viewmodels.AddCityViewModel
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
        viewModel.addEvent.observe(requireActivity(),{
            when(it){
                is ActionEvent.Success-> findNavController().navigate(R.id.homeFragment)
                is ActionEvent.Error -> Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                else -> {}
            }
        })
//        lifecycleScope.launch {
//            viewModel.addEvent.collect {
//                //添加天气回调，但是会出现ui卡顿，准备不用这种方法
//                when(it){
//                    is ActionEvent.Success-> findNavController().navigate(R.id.homeFragment)
//                    is ActionEvent.Error -> Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
//                    else -> {}
//                }
//            }
//        }
    }
}