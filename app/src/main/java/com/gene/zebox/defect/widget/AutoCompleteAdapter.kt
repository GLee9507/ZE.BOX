package com.gene.zebox.defect.widget

import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.lifecycle.LiveData
import com.gene.zebox.defect.model.DefectItem
import kotlinx.coroutines.*


class AutoCompleteAdapter(
    private val list: LiveData<Array<DefectItem>>
) : BaseAdapter() {
    var data: List<CharSequence>? = null
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val v = convertView ?: LayoutInflater.from(parent.context).inflate(
            android.R.layout.simple_list_item_1,
            parent,
            false
        )
        v.findViewById<TextView>(android.R.id.text1).text = getItem(position)
        return v
    }

    override fun getItem(position: Int): CharSequence {
        return data?.get(position) ?: ""
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return data?.size ?: 0
    }

    fun clear() {
        data = null
        notifyDataSetChanged()
    }

    private var job: Job? = null
    fun submit(constraint: CharSequence, block: Runnable) {
        job?.cancel()
        val value = list.value
        job = CoroutineScope(Dispatchers.Main).launch {
            if (value.isNullOrEmpty()) {
                return@launch
            }

            val a = async(Dispatchers.IO) {
                val result = ArrayList<CharSequence>()
                value.forEach item@{
                    var text = it.text
                    var offset = 0
                    val indexList = ArrayList<Int>()
                    constraint.forEach { c ->
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
                        val spannableString = SpannableStringBuilder(it.text)
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
                return@async result
            }
            data = a.await()
            notifyDataSetChanged()
            handler.postDelayed(block,200)
        }
    }

    private val handler by lazy { Handler(Looper.getMainLooper()) }


}