package com.gene.zebox.defect.widget

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.resources.MaterialAttributes
import java.util.*

class MaterialTimePicker(private val listener: (dateTime: Long) -> Unit) :
    DialogFragment() {
    @SuppressLint("RestrictedApi")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val resolve = MaterialAttributes.resolveOrThrow(
            requireContext(),
            com.google.android.material.R.attr.materialCalendarTheme,
            MaterialTimePicker::javaClass.name
        )

        return TimePickerDialog(requireContext(), resolve, { view, hourOfDay, minute ->

            listener.invoke(dateTime + hourOfDay * 60 * 60 * 1000 + minute * 60  * 1000)
        }, -1, -1, true).apply {
            setMessage("请选择超期时间")
            setCanceledOnTouchOutside(false)
            setOnCancelListener {
                //                listener.invoke(false, -1, -1)
            }
            setOnShowListener {
                val hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                val minute = Calendar.getInstance().get(Calendar.MINUTE)
                if (it is TimePickerDialog) {
                    it.updateTime(hourOfDay, minute)
                }

            }
        }
    }

    var dateTime = 0L
    fun showNow(date: Long, manager: FragmentManager) {
        dateTime = date
        showNow(manager, "timePicker")
    }
}