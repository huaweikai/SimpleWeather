package com.hua.simpleweather.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class BaseViewHolder<VB:ViewBinding>(
    val bind:VB
):RecyclerView.ViewHolder(bind.root)