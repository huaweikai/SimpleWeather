package com.hua.simpleweather.ui.fragments

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
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
            onDeleteClick = {
                //点到了删除按钮，执行删除天气
                viewModel.deleteWeatherBean(it)
            },
            onclick = { position ->
                //点到item，回到天气界面，并返回当前的position
                Bundle().also {
                    it.putInt(CITY_TO_HOME, position)
                    findNavController().navigate(
                        R.id.action_cityFragment_to_homeFragment,
                        it)
                }
            }
        )
        //对城市列表接收和设置
        viewModel.cityListLiveData.observe(requireActivity()){
            adapter.submitList(it)
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
                //当需要被滑动的id == 0也就是为定位天气时，不允许被滑动
                if (viewModel.cityListLiveData.value?.get(viewHolder.bindingAdapterPosition)?.id != 0) {
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
                //当移动到 定位天气时，不允许替换位置，保持定位天气永远第一位
                if (viewModel.cityListLiveData.value?.get(target.bindingAdapterPosition)?.id != 0){
                    viewModel.updateCityIndex(viewHolder.bindingAdapterPosition, target.bindingAdapterPosition)
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
                backAction()
            }
        }
    }


    private fun backAction() {
        val graph = findNavController().graph
        if (graph.startDestinationId == R.id.homeFragment) {
            findNavController().navigateUp()
        } else {
            activity?.finish()
        }
    }

}