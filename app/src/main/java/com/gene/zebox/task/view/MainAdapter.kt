package com.gene.zebox.task.view

import android.R
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gene.zebox.App
import com.gene.zebox.task.model.BugItem

class MainAdapter : ListAdapter<BugItem, MainViewHolder>(BugItem.ITEM_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            LayoutInflater.from(App.context).inflate(
                R.layout.simple_list_item_1,
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.textView.text = "${position + 1}. ${getItem(position).text}"
    }

}

data class MainViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val textView: TextView by lazy { view.findViewById<TextView>(android.R.id.text1) }
}
