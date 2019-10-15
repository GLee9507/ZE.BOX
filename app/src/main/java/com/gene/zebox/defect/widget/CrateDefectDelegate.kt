//package com.gene.zebox.defect.widget
//
//import android.annotation.SuppressLint
//import android.app.Dialog
//import android.content.Context
//import android.content.DialogInterface
//import android.graphics.Color
//import android.os.Bundle
//import android.os.Parcel
//import android.os.Parcelable
//import android.text.Spannable
//import android.text.SpannableString
//import android.text.TextPaint
//import android.text.method.LinkMovementMethod
//import android.text.style.ClickableSpan
//import android.view.LayoutInflater
//import android.view.View
//import android.widget.TextView
//import androidx.annotation.MainThread
//import androidx.appcompat.app.AlertDialog
//import androidx.appcompat.app.AppCompatActivity
//import androidx.fragment.app.DialogFragment
//import com.gene.zebox.R
//import com.google.android.material.datepicker.MaterialDatePicker
//import com.google.android.material.resources.MaterialAttributes
//import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
//import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
//
//
//class CrateDefectDelegate(private val context: AppCompatActivity) {
//    data class Info(
//
//        var title: String = "",
//        var hourOfDay: Int = -1,
//        var minute: Int = -1,
//        var year: Int = -1, var monthOfYear: Int = -1, var dayOfMonth: Int = -1
//    ) : Parcelable {
//        constructor(source: Parcel) : this(
//            source.readString() ?: "",
//            source.readInt(),
//            source.readInt(),
//            source.readInt(),
//            source.readInt(),
//            source.readInt()
//        )
//
//        override fun describeContents() = 0
//
//        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
//            writeString(title)
//            writeInt(hourOfDay)
//            writeInt(minute)
//            writeInt(year)
//            writeInt(monthOfYear)
//            writeInt(dayOfMonth)
//        }
//
//        companion object {
//            @JvmField
//            val CREATOR: Parcelable.Creator<Info> = object : Parcelable.Creator<Info> {
//                override fun createFromParcel(source: Parcel): Info = Info(source)
//                override fun newArray(size: Int): Array<Info?> = arrayOfNulls(size)
//            }
//        }
//    }
//
//    var info: Info? = null
//    var result: ((Info) -> Unit)? = null
//    @MainThread
//    fun start(result: (Info) -> Unit) {
//        info = Info()
//        this.result = result
//        dialog.show(context.supportFragmentManager, "1")
//    }
//
//    private val dialog by lazy {
//
//        Dialog1 { dialog, title, confirm ->
//            info?.title = title
//            if (confirm) {
//                datePicker1.show(context.supportFragmentManager, "2")
//                dialog.dismiss()
//            } else {
//                info = null
//                dialog.cancel()
//            }
//        }
//    }
//    private val timePicker by lazy {
//        MaterialTimePicker { confirm, hourOfDay, minute ->
//            if (confirm) {
//                info?.hourOfDay = hourOfDay
//                info?.minute = minute
//                info?.let {
//                    result?.invoke(it)
//                }
//            } else {
//                info = null
//            }
//        }
//    }
//    private val datePicker by lazy {
//        MaterialDatePicker.Builder.datePicker()
//            .setTitleText(R.string.date_picker_title)
//            .build().apply {
//                addOnPositiveButtonClickListener {
//                    timePicker.show(this@CrateDefectDelegate.context.supportFragmentManager, "3")
//                }
//                addOnNegativeButtonClickListener {
//                    info = null
//                }
//            }
//    }
//    private val datePicker1 by lazy {
//        DatePickerDialog.newInstance { view, year, monthOfYear, dayOfMonth ->
//            info?.year = year
//            info?.monthOfYear = monthOfYear
//            info?.dayOfMonth = dayOfMonth
//            timePicker2.show(context.supportFragmentManager, "123")
//        }
//    }
//
//    private val timePicker2 by lazy {
//        TimePickerDialog.newInstance({ view, hourOfDay, minute, second ->
//            info?.hourOfDay = hourOfDay
//            info?.minute = minute
//            info?.let {
//                result?.invoke(it)
//            }
//        }
//            , true)
//    }
//}
//
//class Dialog1(private val listener: (dialog: DialogInterface, title: String, confirm: Boolean) -> Unit) :
//    DialogFragment() {
//    fun dip2px(context: Context, dpValue: Float): Int {
//        val scale = context.resources.displayMetrics.density
//        return (dpValue * scale + 0.5f).toInt()
//    }
//
//    @SuppressLint("RestrictedApi")
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val resolve = MaterialAttributes.resolveOrThrow(
//            requireContext(),
//            com.google.android.material.R.attr.materialAlertDialogTheme,
//            MaterialTimePicker::javaClass.name
//        )
////        val editText = AppCompatEditText(requireContext())
//        val view =
//            LayoutInflater.from(requireContext()).inflate(R.layout.dialog_new_defect_set, null)
////        val num = view.findViewById<TextView>(R.id.num)
////        val loc = view.findViewById<TextView>(R.id.loc)
//        val date = view.findViewById<TextView>(1)
//
//        date.movementMethod = LinkMovementMethod.getInstance()
//        date.text = SpannableString("点击设置截至日期与时间").apply {
//            this.setSpan(object : ClickableSpan() {
//                override fun onClick(widget: View) {
//                    DatePickerDialog.newInstance { view, year, monthOfYear, dayOfMonth -> }
//                        .show(this@Dialog1.childFragmentManager, "11")
//                }
//
//                override fun updateDrawState(ds: TextPaint) {
//                    ds.color = Color.BLUE
//                    super.updateDrawState(ds)
//                }
//            }, 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//        }
////        editText.setPadding(dip2px(requireContext(), 10.toFloat()))
//        return AlertDialog.Builder(
//            context!!,
//            resolve
//        ).setTitle("新建缺陷集")
//            .setMessage("请输入标题")
//            .setCancelable(false)
//            .setView(view)
//            .create().apply {
//                setButton(DialogInterface.BUTTON_POSITIVE, "下一步", null, null)
//                setButton(
//                    DialogInterface.BUTTON_NEGATIVE,
//                    "取消", null, null
//                )
//                setOnShowListener {
//                    this.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
//                        //                        val toString = editText.text.toString().trim()
////                        listener.invoke(this, toString, true)
//                    }
//                    this.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener {
//                        listener.invoke(this, Int.MIN_VALUE.toString(), false)
//                    }
//                }
//            }
//
//    }
//}