package com.gene.zebox.defect.widget

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.gene.zebox.R
import com.gene.zebox.WeakRefLazy
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.dialog_new_defect_set.view.*


class CreateDefectSetDialog : DialogFragment(), View.OnClickListener {

    private val dialog by WeakRefLazy {
        @SuppressLint("InflateParams")
        val content = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_new_defect_set, null)
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("创建缺陷集")
            .setView(
                content
            )
            .setPositiveButton("好", null)
            .setNegativeButton("不好", null)
            .create().apply {
                setOnShowListener {
                    val realDialog = it as Dialog
                    realDialog.window?.findViewById<View>(android.R.id.icon)?.parent?.let { view ->
                        if (view is View) {
                            content.setPadding(
                                view.paddingLeft,
                                view.paddingTop,
                                view.paddingRight,
                                view.paddingBottom
                            )
                        }
                    }
                    content.btnDate.setOnClickListener(this@CreateDefectSetDialog)
                    getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(this@CreateDefectSetDialog)
                    getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(this@CreateDefectSetDialog)
                }
                setOnDismissListener {
                    content.btnDate.setOnClickListener(null)
                    getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(null)
                    getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(null)
                }
            }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return dialog
    }

    fun reset() {

    }

    private val datePicker by WeakRefLazy<MaterialDatePicker<Long>> {
        MaterialDatePicker.Builder
            .datePicker()
            .setTitleText("请选择超期日期")
            .build().apply {
                this.addOnPositiveButtonClickListener {
                    timePicker.showNow(this@CreateDefectSetDialog.childFragmentManager, "time")
                }
            }
    }

    private val timePicker by WeakRefLazy {
        MaterialTimePicker { _, hourOfDay, minute ->

        }
    }

    override fun onClick(v: View) {
        when {
            v.id == R.id.btnDate -> {
                datePicker.showNow(childFragmentManager, "date")
            }

            v is Button && v.text == "好" -> {
                reset()
            }

            v is Button && v.text == "不好" -> {
                reset()
            }
        }
    }
}