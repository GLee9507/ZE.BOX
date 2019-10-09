package com.gene.zebox.defect.widget

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gene.zebox.R
import com.gene.zebox.defect.model.DefectItem
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList


typealias RunAfter = (hasData: Boolean) -> Unit
typealias OnItemClick = (data: DefectItem, selectOrEdit: Boolean) -> Unit

class SuggestAdapter :
    RecyclerView.Adapter<SuggestViewHolder>() {

    var clickListener: OnItemClick? = null

    private val itemClickListener by lazy {
        View.OnClickListener {
            if (it.id == R.id.btnEdit) {
                clickListener?.invoke(
                    (it.parent.parent.parent as View).getTag(R.integer.suggestItemData) as DefectItem,
                    false
                )
            } else {
                clickListener?.invoke(
                    it.getTag(R.integer.suggestItemData) as DefectItem,
                    true
                )

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_suggest, parent, false)
        return SuggestViewHolder(view, itemClickListener)
    }

    override fun onBindViewHolder(holder: SuggestViewHolder, position: Int) {
        holder.bindData(data?.get(position) ?: DefectItem.DEFAULT)
    }


    private lateinit var recyclerView: RecyclerView
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    internal var value: Array<DefectItem>? = null

    private var job: Job? = null
    fun submit(str: CharSequence, block: RunAfter) {
        job?.cancel()
        val value = this.value
        val input = str.trim()
        if (input.isEmpty() || value.isNullOrEmpty()) {
            block.invoke(false)
            data = null
            notifyDataSetChanged()
            return
        }
//        if (value.isNullOrEmpty()) {
//            data = null
//            block.invoke(false)
//            return
//        }
        job = CoroutineScope(Dispatchers.Main).launch {
            var hasData = false

            val render = async(Dispatchers.IO) {
                val result = ArrayList<DefectItem>()
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
                        result.add(it.clone().apply {
                            this.render = spannableString
                        })
                    }
                }
                if (result.isNotEmpty()) {
                    hasData = true
                }
                result.sortByDescending { it.count }

                val db = object : DiffUtil.Callback() {
                    override fun getOldListSize() = data?.size ?: 0
                    override fun getNewListSize() = result.size
                    override fun areItemsTheSame(
                        oldItemPosition: Int,
                        newItemPosition: Int
                    ) =
                        data!![oldItemPosition].text == result[newItemPosition].text

                    override fun areContentsTheSame(
                        oldItemPosition: Int,
                        newItemPosition: Int
                    ) = Objects.equals(
                        data!![oldItemPosition].render,
                        result[newItemPosition].render
                    )

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

    private var data: Array<DefectItem>? = null

}

class SuggestViewHolder(private val view: View, listener: View.OnClickListener) :
    RecyclerView.ViewHolder(view) {
    private val tv by lazy { view.findViewById<TextView>(R.id.content) }
    private val btnEdit by lazy { view.findViewById<MaterialButton>(R.id.btnEdit) }
    private var data: DefectItem? = null
    fun bindData(data: DefectItem) {
        this.data = data
        tv.text = data.render
//        view.setTag(R.integer.suggestItemPos, adapterPosition)
        view.setTag(R.integer.suggestItemData, data)
    }

    init {
        view.setOnClickListener(listener)
        btnEdit.setOnClickListener(listener)
    }

}


