package com.gene.zebox.defect.widget

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.PopupWindow
import androidx.core.widget.PopupWindowCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gene.zebox.defect.model.DefectItem
import com.gene.zebox.defect.view.MainAdapter

class ListPopupWindow(private val attachView: View) {
    private val adapter by lazy {
        MainAdapter()
    }

    private val popupWindow: PopupWindow by lazy {
        PopupWindow(
            RecyclerView(attachView.context).apply {
                layoutManager = LinearLayoutManager(context)
                adapter = this@ListPopupWindow.adapter
                setBackgroundColor(Color.LTGRAY)
            }, -1, -1
        )
    }

    fun submitAndShow(data: List<DefectItem>?) {
        if (data.isNullOrEmpty()) {
            dismiss()
        } else {
            adapter.submitList(data) {
                show()
            }
        }
    }

    fun show() {
        PopupWindowCompat.showAsDropDown(popupWindow, attachView, 0, 0, Gravity.CENTER)
    }

    fun dismiss() {
        popupWindow.dismiss()
    }

    fun isShowing() = popupWindow.isShowing

    fun setOnItemClickListener(listener: (data: DefectItem) -> Unit) {
        adapter.clickListener = listener
    }

}