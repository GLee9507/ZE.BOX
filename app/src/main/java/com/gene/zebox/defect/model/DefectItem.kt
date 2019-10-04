package com.gene.zebox.defect.model

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "defect_item")
class DefectItem(
    @PrimaryKey
    var text: String,
    var letter: String,
    var timestamp: Long = 0,
    //使用次数
    var count: Long = 0
) : Comparator<DefectItem> {
    override fun compare(o1: DefectItem, o2: DefectItem): Int {
        return (o1.count - o2.count).toInt()
    }

    @Ignore
    var render: CharSequence? = null

    override fun toString(): String {
        return text
    }

    companion object {
        val DEFAULT = DefectItem("", "")
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
