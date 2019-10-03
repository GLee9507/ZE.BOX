package com.gene.zebox.defect.model

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "defect_item")
open class DefectItem(
    @PrimaryKey
    var text: String,
    var letter: String,
    var timestamp: Long = 0,
    //使用次数
    var count: Long = 0
) {
    override fun toString(): String {
        return text
    }
    companion object {

        val ITEM_CALLBACK = object : DiffUtil.ItemCallback<DefectItem>() {
            override fun areItemsTheSame(oldItem: DefectItem, newItem: DefectItem): Boolean {
                return oldItem.text == newItem.text
            }

            override fun areContentsTheSame(oldItem: DefectItem, newItem: DefectItem): Boolean {
                return oldItem.text == newItem.text
            }
        }

    }
}
