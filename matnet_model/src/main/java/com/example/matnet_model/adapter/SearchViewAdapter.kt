package com.example.matnet_model.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.matnet_model.R
import com.example.matnet_model.bean.MarNetLinkBean

class SearchViewAdapter : BaseQuickAdapter<MarNetLinkBean, BaseViewHolder>(R.layout.viewholder_search) {
    override fun convert(helper: BaseViewHolder?, item: MarNetLinkBean?) {
        helper?.let { viewHolder->
            viewHolder.itemView.findViewById<TextView>(R.id.tvTitle).text = item?.title
            viewHolder.itemView.findViewById<TextView>(R.id.tvType).text = item?.type
            viewHolder.itemView.findViewById<TextView>(R.id.tvAdress).text = item?.link
            viewHolder.addOnLongClickListener(R.id.con)
        }
    }
}