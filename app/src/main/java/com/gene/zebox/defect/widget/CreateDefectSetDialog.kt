package com.gene.zebox.defect.widget

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.gene.zebox.R
import java.lang.ref.WeakReference


class CreateDefectSetDialog : DialogFragment() {

    private val viewRef by lazy { WeakReference<View>(null) }
    private val dialogRef by lazy { WeakReference<Dialog>(null) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = initView()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return if (dialogRef.get() == null)
            AlertDialog.Builder(requireContext())
                .setTitle("创建缺陷集")
                .setPositiveButton("好", null)
                .setNegativeButton("不好", null)
                .setView(initView())
                .create()
        else
            dialogRef.get()!!
    }

    private fun initView(): View {
        return if (viewRef.get() == null)
            LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_new_defect_set, null, false)
        else
            viewRef.get()!!
    }

    private fun initDialog(dialog: Dialog) {
        dialog.setTitle("创建缺陷集")
    }

}