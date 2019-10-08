package com.gene.zebox.defect.widget

import android.content.Context
import android.util.AttributeSet
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import com.gene.zebox.defect.model.DefectItem

class SuggestView : RecyclerView {

    constructor(context: Context) : super(context)


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    private val adapter = SuggestAdapter()
    fun bind(data: LiveData<Array<DefectItem>>, lifecycleOwner: LifecycleOwner) {
        setAdapter(adapter)
        data.observe(lifecycleOwner) {
            adapter.value = it
        }
    }

    fun query(string: String, block: RunAfter) {
        adapter.submit(string, block)
    }

    fun setOnItemClickListener(listener: (data: DefectItem) -> Unit) {
        adapter.clickListener = listener
    }

}