package com.gene.zebox.task.model

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bug_item")
open class BugItem(
    @PrimaryKey
    val text: String,
    val letter: String,
    val timestamp: Long = 0
) {
//    @PrimaryKey(autoGenerate = true)
//    var _id: Long? = null

    companion object {

        val ITEM_CALLBACK = object : DiffUtil.ItemCallback<BugItem>() {
            override fun areItemsTheSame(oldItem: BugItem, newItem: BugItem): Boolean {
                return oldItem.text == newItem.text
            }

            override fun areContentsTheSame(oldItem: BugItem, newItem: BugItem): Boolean {
                return oldItem.text == newItem.text
            }
        }

    }
}

//class ResultBugItem(val searchKey: String, bugItem: BugItem) :
//    BugItem(bugItem.text, bugItem.letter) {
//}