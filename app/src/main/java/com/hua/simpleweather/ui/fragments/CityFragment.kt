package com.hua.simpleweather.ui.fragments

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hua.network.Contacts.CITY_TO_HOME
import com.hua.simpleweather.R
import com.hua.simpleweather.base.BaseFragment
import com.hua.simpleweather.databinding.FragmentCityBinding
import com.hua.simpleweather.ui.adapter.CityAdapter
import com.hua.simpleweather.ui.viewmodels.MainViewModel
import com.hua.simpleweather.utils.dp
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class CityFragment : BaseFragment<FragmentCityBinding>() {

    private val viewModel by activityViewModels<MainViewModel>()
    private var sortedCity = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initViewBind(FragmentCityBinding.inflate(layoutInflater))
        sortedCity = false
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind.citySearch.setOnClickListener {
            //虚假的搜索按钮
            findNavController().navigate(R.id.action_cityFragment_to_addCityFragment)
        }
        val adapter = CityAdapter(
            onDeleteClick = {
                //点到了删除按钮
                viewModel.deleteWeatherBean(it)
            },
            onclick = { position ->
                //点到item，回到天气界面
                Bundle().also {
                    it.putInt(CITY_TO_HOME, position)
                    findNavController().navigate(
                        R.id.action_cityFragment_to_homeFragment,
                        it)
                }
            },
            onLongClick = {
                //长按已被弃用
                //如果adapter某一个长按，将确认键显示
//                bind.cityOk.isVisible = true
            }
        )
        viewModel.cityListLiveData.observe(requireActivity()){
            (adapter.submitList(it))
        }
        bind.cityRv.apply {
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(requireContext())
            this.addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    outRect.top = 8.dp
                    outRect.bottom = 8.dp
                    outRect.left = 16.dp
                    outRect.right = 16.dp
                }
            })
        }

        val helper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                if (viewHolder.adapterPosition != 0) {
                    val dragFlag = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                    return makeMovementFlags(dragFlag, 0)
                }
                return makeMovementFlags(0, 0)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                if (target.adapterPosition != 0){
                    viewModel.updateCity(viewHolder.adapterPosition, target.adapterPosition)
                    sortedCity = true
                }
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            }
        })
        helper.attachToRecyclerView(bind.cityRv)

        bind.cityOk.setOnClickListener {
            if (adapter.isDeleteTime) {
                bind.cityOk.setImageResource(R.drawable.ic_more)
            } else {
                bind.cityOk.setImageResource(R.drawable.ic_check)
            }
            adapter.isDeleteTime = !adapter.isDeleteTime
        }

        bind.cityDesc.apply {
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if(sortedCity){
            viewModel.replaceWeatherSort()
        }

    }

}