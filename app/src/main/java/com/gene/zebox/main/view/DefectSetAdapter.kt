package com.gene.zebox.main.view

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gene.zebox.DATA_FORMAT
import com.gene.zebox.R
import com.gene.zebox.main.model.DefectSet
import com.google.android.material.textview.MaterialTextView

class DefectSetAdapter : ListAdapter<DefectSet, DefectSetViewHolder>(ITEM_CALLBACK) {
    private var layoutInflater: LayoutInflater? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefectSetViewHolder {
        layoutInflater = layoutInflater ?: LayoutInflater.from(parent.context)
        val view = layoutInflater!!.inflate(R.layout.item_main, parent, false)
        return DefectSetViewHolder(view)
    }

    override fun onBindViewHolder(holder: DefectSetViewHolder, position: Int) {
        holder.binData(getItem(position))
    }


    companion object {
        val ITEM_CALLBACK = object : DiffUtil.ItemCallback<DefectSet>() {
            override fun areItemsTheSame(oldItem: DefectSet, newItem: DefectSet) =
                oldItem.createTime == oldItem.createTime


            override fun areContentsTheSame(oldItem: DefectSet, newItem: DefectSet): Boolean {
                val oldItems = oldItem.defects
                val newItems = newItem.defects
                if (oldItems.size == newItems.size) {
                    if (oldItems.isEmpty()) {
                        return true
                    }
                    for (index in oldItems.size - 1 downTo 0) {
                        if (oldItems[index].id != newItems[index].id) {
                            return false
                        }
                    }
                    return true
                } else {
                    return false
                }
            }

        }
    }
}

class DefectSetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val loc by lazy(LazyThreadSafetyMode.NONE) { view.findViewById<MaterialTextView>(R.id.loc) }
    private val num by lazy(LazyThreadSafetyMode.NONE) { view.findViewById<MaterialTextView>(R.id.num) }
    private val content by lazy(LazyThreadSafetyMode.NONE) { view.findViewById<MaterialTextView>(R.id.content) }
    private val time by lazy(LazyThreadSafetyMode.NONE) { view.findViewById<MaterialTextView>(R.id.time) }

    fun binData(data: DefectSet) {
        if (adapterPosition%2==0) {
            itemView.setBackgroundColor(Color.YELLOW)
        }else{

            itemView.setBackgroundColor(Color.GRAY)
        }
        loc.text = data.location
        num.text = data.number
        time.text = DATA_FORMAT.format(data.timeout)
    }
}