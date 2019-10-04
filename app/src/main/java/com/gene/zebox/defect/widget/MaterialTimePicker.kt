package com.gene.zebox.defect.widget

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.resources.MaterialAttributes
import java.util.*

class MaterialTimePicker(val listener: (confirm: Boolean, hourOfDay: Int, minute: Int) -> Unit) :
    DialogFragment() {
    @SuppressLint("RestrictedApi")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val resolve = MaterialAttributes.resolveOrThrow(
            requireContext(),
            com.google.android.material.R.attr.materialCalendarTheme,
            MaterialTimePicker::javaClass.name
        )

        return TimePickerDialog(requireContext(), resolve, { view, hourOfDay, minute ->
            listener.invoke(true, hourOfDay, minute)
        }, hourOfDay, minute, true).apply {
            setMessage("请选择超期时间")
            setCanceledOnTouchOutside(false)
            setOnCancelListener {
                listener.invoke(false, -1, -1)
            }
        }
    }

    private var hourOfDay = -1
    private var minute = -1
    fun show(manager: FragmentManager) {
        hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        minute = Calendar.getInstance().get(Calendar.MINUTE)
        dialog?.let {
            val timePickerDialog = it as TimePickerDialog
            timePickerDialog.updateTime(hourOfDay, minute)
        }
        super.show(manager, "123")
    }
}