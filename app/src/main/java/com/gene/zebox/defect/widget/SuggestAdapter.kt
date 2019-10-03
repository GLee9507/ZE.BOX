package com.gene.zebox.defect.widget

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gene.zebox.defect.model.DefectItem
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

class SuggestAdapter(private val allList: LiveData<Array<DefectItem>>) :
    RecyclerView.Adapter<SuggestViewHolder>() {


    var clickListener: ((data: DefectItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return SuggestViewHolder(view)
    }

    override fun onBindViewHolder(holder: SuggestViewHolder, position: Int) {
            holder.bindData(data?.get(position ) ?: "")
    }

    private lateinit var recyclerView: RecyclerView
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }


    private var job: Job? = null
    fun submit(input: CharSequence, block: (hasData: Boolean) -> Unit) {
        job?.cancel()
        val value = allList.value
        if (value.isNullOrEmpty()) {
            return
        }
        job = CoroutineScope(Dispatchers.Main).launch {
            var hasData = false
            val render = async(Dispatchers.IO) {
                val result = ArrayList<CharSequence>()
                value.forEach item@{
                    var text = it.text
                    var offset = 0
                    val indexList = ArrayList<Int>()
                    input.forEach { c ->
                        val indexOf = text.indexOf(c)
                        if (indexOf >= 0) {
                            offset += indexOf
                            indexList += offset
                            text = text.substring(indexOf)
                        } else {
                            return@item
                        }
                    }
                    if (indexList.size > 0) {
                        val spannableString = SpannableString(it.text)
                        for (i in indexList) {
                            spannableString.setSpan(
                                ForegroundColorSpan(Color.RED),
                                i,
                                i + 1,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                        }
                        result.add(spannableString)
                    }
                }
                if (result.isNotEmpty()) {
                    hasData = true
                }
                val db = object : DiffUtil.Callback() {
                    override fun getOldListSize() = data?.size ?: 0
                    override fun getNewListSize() = result.size
                    override fun areItemsTheSame(
                        oldItemPosition: Int,
                        newItemPosition: Int
                    ) = data!![oldItemPosition].toString() == result[newItemPosition].toString()

                    override fun areContentsTheSame(
                        oldItemPosition: Int,
                        newItemPosition: Int
                    ) = Objects.equals(data!![oldItemPosition], result[newItemPosition])

                    override fun getChangePayload(
                        oldItemPosition: Int,
                        newItemPosition: Int
                    ): Any? {
                        return true
                    }
                }
                val calculateDiff = DiffUtil.calculateDiff(db)

                return@async Pair(
                    result.toTypedArray(), calculateDiff
                )
            }

            val pair = render.await()
            data = pair.first
            recyclerView.postOnAnimation {
                recyclerView.smoothScrollToPosition(0)
            }
            pair.second.dispatchUpdatesTo(this@SuggestAdapter)

            block.invoke(hasData)
        }

    }

    private var data: Array<CharSequence>? = null

}

class SuggestViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val tv by lazy { view.findViewById<TextView>(android.R.id.text1) }
    fun bindData(data: CharSequence) {
        tv.text = data
    }


}


