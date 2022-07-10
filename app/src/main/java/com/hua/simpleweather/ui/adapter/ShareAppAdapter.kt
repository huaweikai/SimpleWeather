package com.hua.simpleweather.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hua.model.AppInfoVo
import com.hua.simpleweather.base.BaseViewHolder
import com.hua.simpleweather.databinding.ShareAppItemBinding

class ShareAppAdapter:RecyclerView.Adapter<BaseViewHolder<ShareAppItemBinding>>() {
    private var appList:List<AppInfoVo> = listOf()

    fun setData(appInfoList: List<AppInfoVo>){
        appList = appInfoList
        notifyItemChanged(0,appList.size)
    }

    private var onClick:((AppInfoVo) ->Unit) ?= null

    fun setOnClickListener(onClickListener:(AppInfoVo)->Unit){
        this.onClick = onClickListener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ShareAppItemBinding> {
       return BaseViewHolder(
           ShareAppItemBinding.inflate(
               LayoutInflater.from(parent.context),
               parent,
               false
           )
       ).apply {
           bind.root.setOnClickListener {
               onClick?.invoke(appList[this.adapterPosition])
           }
       }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ShareAppItemBinding>, position: Int) {
        holder.bind.apply {
            shareAppIcon.setImageDrawable(appList[position].icon)
            shareAppName.text = appList[position].appName
        }
    }

    override fun getItemCount() = appList.size
}