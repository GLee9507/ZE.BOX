package com.gene.zebox.defect.view

import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.gene.zebox.R
import com.gene.zebox.databinding.ActivityNewTaskBinding
import com.gene.zebox.defect.viewmodel.DefectViewModel
import com.gene.zebox.defect.widget.ListPopupWindow
import kotlinx.android.synthetic.main.activity_new_task.*

class DefectActivity : AppCompatActivity() {
    private lateinit var mainAdapter: MainAdapter

    val binding by lazy {
        DataBindingUtil.setContentView<ActivityNewTaskBinding>(
            this,
            R.layout.activity_new_task
        )
    }
    private val vm by lazy { ViewModelProviders.of(this)[DefectViewModel::class.java] }
    private val popupWindow by lazy { ListPopupWindow(binding.edit) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_task)
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
            }
            return@setOnEditorActionListener true
        }

        vm.selectResult.observe(this, Observer { mainAdapter.submitList(it) })
    }
}




