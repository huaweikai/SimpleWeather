package com.hua.simpleweather.ui.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hua.simpleweather.utils.dp

//共用的，没必要的多实例化
val moduleItemDecoration = ModuleItemDecoration()

class ModuleItemDecoration:RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = 8.dp
        outRect.left = 8.dp
        outRect.right = 8.dp
    }
}