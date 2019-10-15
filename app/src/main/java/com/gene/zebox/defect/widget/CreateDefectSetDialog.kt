package com.gene.zebox.defect.widget

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.gene.zebox.DATA_FORMAT
import com.gene.zebox.R
import com.gene.zebox.WeakRefLazy
import com.gene.zebox.defect.view.DefectActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.dialog_new_defect_set.view.*


class CreateDefectSetDialog : DialogFragment(), View.OnClickListener {
    data class DialogHolder(val dialog: Dialog, val content: View) {
        protected fun finalize() {
            Log.d("init", "$this-gc")
        }
    }

    private val dialogHolder by WeakRefLazy(this) {
        Log.d("glee", "init ")
        @SuppressLint("InflateParams")
        val content = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_new_defect_set, null)
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("创建缺陷集")
            .setView(
                content
            )
            .setPositiveButton("好", null)
            .setNegativeButton("不好", null)
            .create().apply {
                this.setCanceledOnTouchOutside(false)
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
        DialogHolder(dialog, content)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return dialogHolder.dialog
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.get(BTN_TIME_KEY).apply {
            if (this is Long) {
                timestamp = this
                dialogHolder.content.btnDate.text = DATA_FORMAT.format(this)
            }
        }
    }

    private fun reset() {
        dialogHolder.content.apply {
            editNum.setText("")
            editLoc.setText("")
            btnDate.setText(R.string.btn_set_date)
            timestamp = null
        }
    }


    private val datePicker by WeakRefLazy(this) {
        MaterialDatePicker.Builder
            .datePicker()
            .setTitleText("请选择超期日期")
            .build().apply {
                this.addOnPositiveButtonClickListener {
                    timePicker.showNow(it, this@CreateDefectSetDialog.childFragmentManager)
                }
            }
    }
    var timestamp: Long? = null
    private val timePicker by WeakRefLazy(this) {
        MaterialTimePicker {
            timestamp = it
            val format = DATA_FORMAT.format(it)
            Toast.makeText(requireContext(), format, Toast.LENGTH_LONG).show()
            dialogHolder.content.btnDate.text = format
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        timestamp?.apply { outState.putLong(BTN_TIME_KEY, this) }
        super.onSaveInstanceState(outState)
    }

    override fun onClick(v: View) {
        when {
            v.id == R.id.btnDate -> {
                datePicker.showNow(childFragmentManager, "date")
            }

            v is Button && v.text == "好" -> {
                startActivity(Intent(requireContext(), DefectActivity::class.java).apply {
                    putExtra(BTN_TIME_KEY, timestamp)
                    putExtra(EDIT_NUM, dialogHolder.content.editNum.text.toString())
                    putExtra(EDIT_LOC, dialogHolder.content.editLoc.text.toString())
                })
                dismiss()
                reset()

            }

            v is Button && v.text == "不好" -> {
                reset()
                dismiss()
            }
        }
    }

    companion object {
        private const val BTN_TIME_KEY = "date.btn.timestamp"
        private const val EDIT_NUM = "date.edit.num"
        private const val EDIT_LOC = "date.edit.loc"
    }
}