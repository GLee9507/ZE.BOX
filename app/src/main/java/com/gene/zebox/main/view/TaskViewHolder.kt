package com.gene.zebox.main.view

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gene.zebox.R

class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val title: TextView by lazy { view.findViewById(R.id.title) }
    val time: TextView by lazy { view.findViewById(R.id.time) }
    val content: TextView by lazy { view.findViewById(R.id.content) }
}