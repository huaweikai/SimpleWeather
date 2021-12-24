package com.hua.simpleweather.ui.fragments

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.hua.simpleweather.R
import com.hua.simpleweather.base.BaseFragment
import com.hua.simpleweather.databinding.FragmentCityBinding
import com.hua.simpleweather.other.Contacts.CITY_TO_HOME
import com.hua.simpleweather.ui.adapter.CityAdapter
import com.hua.simpleweather.ui.viewmodels.MainViewModel
import com.hua.simpleweather.utils.dp
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
                //长按已被弃用
                //如果adapter某一个长按，将确认键显示
//                bind.cityOk.isVisible = true
            }
        )

        bind.cityRv.apply {
            this.adapter = adapter
            this.layoutManager = GridLayoutManager(requireContext(), 2)
            this.addItemDecoration(object :RecyclerView.ItemDecoration(){
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    outRect.bottom = 20.dp
                    outRect.left = 12.dp
//                    outRect.right = 6.dp
                }
            })
        }

        lifecycleScope.launch {
            viewModel.roomWeather.collect {
                adapter.submitList(it)
            }
        }
        val helper = ItemTouchHelper(object :ItemTouchHelper.Callback(){
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlag = ItemTouchHelper.UP or ItemTouchHelper.DOWN or
                        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                return makeMovementFlags(dragFlag,0)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val actionData = adapter.currentList[viewHolder.adapterPosition]
                val targetData = adapter.currentList[target.adapterPosition]
                viewModel.updateWeather(actionData.copy(id = target.adapterPosition))
                viewModel.updateWeather(targetData.copy(id = viewHolder.adapterPosition))
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            }
        }
        )
        helper.attachToRecyclerView(bind.cityRv)

        bind.cityOk.setOnClickListener {
            if(adapter.isDeleteTime){
                adapter.isDeleteTime = false
                bind.cityOk.setImageResource(R.drawable.ic_more)
            }else{
                adapter.isDeleteTime = true
                bind.cityOk.setImageResource(R.drawable.ic_check)
            }
//            //如果删除完毕，再次更新adapter，让删除图标消失,同时让确认图标消失
//            adapter.isDeleteTime = false
//            adapter.notifyItemRangeChanged(0, adapter.itemCount)
//            bind.cityOk.isVisible = false
        }
    }

}