package com.hua.simpleweather.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hua.network.Contacts.CITY_TO_HOME
import com.hua.simpleweather.ActionEvent.*
import com.hua.simpleweather.R
import com.hua.simpleweather.base.BaseFragment
import com.hua.simpleweather.databinding.FragmentAddCityBinding
import com.hua.simpleweather.db.dao.bean.LocalCity
import com.hua.simpleweather.ui.adapter.PagingCityAdapter
import com.hua.simpleweather.ui.adapter.moduleItemDecoration
import com.hua.simpleweather.ui.viewmodels.AddCityViewModel
import com.hua.simpleweather.utils.setStartDestination
import com.hua.simpleweather.utils.toast
import dagger.hilt.android.AndroidEntryPoint
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
        val adapter = PagingCityAdapter {
            showDiaLog(it)
        }
        //对 EdtextView的值进行监听，实时检索位置信息
        val watch = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                //检索相关的位置信息
//                viewModel.searchPlace(s.toString())
                viewModel.key = s.toString()
                adapter.refresh()
            }
        }
        bind.addCityEd.addTextChangedListener(watch)
        bind.addCityToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        bind.addrv.apply {
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(requireContext())
            this.addItemDecoration(moduleItemDecoration)
        }

        //监听检索到的城市列表
        lifecycleScope.launch {
            viewModel.pagingSelectPaging.collect {
                adapter.submitData(it)
            }
        }
        //对本地已有的城市进行更新
        //属于残留代码，现在如果添加成功，就会直接返回到天气界面，只需要一个列表即可，不用监听
        lifecycleScope.launch {
            viewModel.localCity.collect {
                adapter.updateLocalList(it)
            }
        }

        //对添加事件进行监听
        lifecycleScope.launch {
            viewModel.addEvent.collect {
                when (it) {
                    is Error -> it.message.toast(requireContext())
                    is Success -> {
                        "添加成功".toast(requireContext())
                        actionToHomeFragment()
                    }
                    else -> {}
                }
            }
        }
    }

    //展示一个dialog，用于确定是否添加
    private fun showDiaLog(city: LocalCity) {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("添加城市")
            setMessage("确认添加 ${city.cityName}吗")
            setPositiveButton(
                "确定"
            ) { _, _ -> viewModel.addCity(city) }
            setNegativeButton(
                "取消"
            ) { _, _ -> }
            create().show()
        }
    }

    private fun actionToHomeFragment() {
        val bundle = Bundle()
        bundle.putInt(CITY_TO_HOME, -1)
        //成功后，将跳转到天气界面
        val navController = findNavController()
        if (navController.graph.startDestinationId == R.id.homeFragment) {
            navController.navigate(R.id.action_addCityFragment_to_homeFragment, bundle)
        } else {
            navController.setStartDestination(
                R.navigation.navi,
                R.id.homeFragment,
                bundle
            )
        }
    }
}