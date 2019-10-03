package com.gene.zebox.defect.widget

import android.view.Gravity
import android.view.View
import android.widget.PopupWindow
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.widget.PopupWindowCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gene.zebox.defect.model.DefectItem

class ListPopupWindow(private val attachView: View) {
    private val adapter by lazy {
        MainAdapter()
    }

    private val popupWindow: PopupWindow by lazy {
        PopupWindow(
            RecyclerView(
                ContextThemeWrapper(
                    attachView.context,
                    androidx.appcompat.R.style.Widget_AppCompat_ListPopupWindow
                )
            ).apply {
                layoutManager = LinearLayoutManager(context)
                adapter = this@ListPopupWindow.adapter
            }, -1, -1
        ).apply { }
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