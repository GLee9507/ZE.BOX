package com.gene.zebox.defect.view

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.gene.zebox.R
import com.gene.zebox.databinding.ActivityNewTaskBinding
import com.gene.zebox.defect.viewmodel.DefectViewModel
import com.gene.zebox.defect.widget.ListPopupWindow
import kotlinx.android.synthetic.main.activity_new_task.*


class DefectActivity : AppCompatActivity() {
    private lateinit var mainAdapter: MainAdapter

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityNewTaskBinding>(
            this,
            R.layout.activity_new_task
        )
    }
    private val vm by lazy { ViewModelProviders.of(this)[DefectViewModel::class.java] }

    private val popupWindow by lazy {
        ListPopupWindow(binding.edit).apply {
            setOnItemClickListener {
                binding.vm?.add2Selected(it)
                binding.edit.setText("")
                dismiss()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        binding.vm = vm
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = MainAdapter()
        mainAdapter = recycler.adapter as MainAdapter
        edit.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null &&
                        event.keyCode == KeyEvent.KEYCODE_ENTER &&
                        event.action == KeyEvent.ACTION_UP)
            ) {
                vm.newItem()
                binding.edit.setText("")
            }
            return@setOnEditorActionListener true
        }
        vm.queryResult.observe(this) { popupWindow.submitAndShow(it?.toList()) }
        vm.selectResult.observe(this) {
            mainAdapter.submitList(it?.toMutableList())
        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onBackPressed() {
        if (popupWindow.isShowing()) {
            popupWindow.dismiss()
        } else
            super.onBackPressed()
    }
}




