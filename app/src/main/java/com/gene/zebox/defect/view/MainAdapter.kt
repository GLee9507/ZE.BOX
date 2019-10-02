package com.gene.zebox.defect.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gene.zebox.App
import com.gene.zebox.defect.model.DefectItem

class MainAdapter : ListAdapter<DefectItem, MainViewHolder>(DefectItem.ITEM_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            LayoutInflater.from(App.CONTEXT).inflate(
                android.R.layout.simple_list_item_1,
                parent,
                false
            ), this
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }


    var clickListener: ((data: DefectItem) -> Unit)? = null

}

class MainViewHolder(view: View, adapter: MainAdapter) : RecyclerView.ViewHolder(view) {
    private val textView: TextView by lazy {
        view.findViewById<TextView>(android.R.id.text1)
    }
    private lateinit var data: DefectItem
    @SuppressLint("SetTextI18n")
    fun bindData(data: DefectItem) {
        this.data = data
        textView.text = data.text+"--"+data.count.toString()
    }

    init {
        view.setOnClickListener {
            adapter.clickListener?.invoke(data)
        }
    }
}