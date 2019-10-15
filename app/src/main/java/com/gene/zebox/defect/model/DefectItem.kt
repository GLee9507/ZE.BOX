package com.gene.zebox.defect.model

import android.text.SpannableString
import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "defect_item")
class DefectItem(
    var text: String,
    var letter: String,
    var timestamp: Long = 0,
//使用次数
    var count: Long = 0
) :  Cloneable {


    public override fun clone(): DefectItem {
        return DefectItem(text, letter, timestamp, count).apply {
            this.render = this@DefectItem.render
        }
    }
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
    @Ignore
    var render: SpannableString? = null

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
