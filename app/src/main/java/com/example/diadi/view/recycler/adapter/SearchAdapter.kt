package com.example.diadi.view.recycler.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.diadi.R
import com.example.diadi.view.recycler.item.SearchLayout

class SearchAdapter(val itemList: ArrayList<SearchLayout>) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType : Int) : ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.name.text = itemList[position].name
        holder.category.text = itemList[position].category
        holder.address.text = itemList[position].address

        // 아이템 클릭 이벤트 리스너 추가
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.place_name)
        val category: TextView = itemView.findViewById(R.id.place_category)
        val address: TextView = itemView.findViewById(R.id.place_road_address)
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    private lateinit var itemClickListener : OnItemClickListener
}


