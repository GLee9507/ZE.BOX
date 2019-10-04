package com.gene.zebox.defect.view

import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.gene.zebox.R
import com.gene.zebox.databinding.ActivityNewTaskBinding
import com.gene.zebox.defect.viewmodel.DefectViewModel
import com.gene.zebox.defect.widget.CrateDefectDelegate
import com.gene.zebox.defect.widget.MainAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_new_task.*


class DefectActivity : AppCompatActivity() {

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityNewTaskBinding>(
            this,
            R.layout.activity_new_task
        )
    }
    private val vm by lazy { ViewModelProviders.of(this)[DefectViewModel::class.java] }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val info = intent.getParcelableExtra<CrateDefectDelegate.Info>("info")!!
        initToolBar(info)
        binding.lifecycleOwner = this
        binding.vm = vm
        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.adapter = MainAdapter()
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


        vm.edit.observe(this) {

            binding.suggest.query(it) { hasData ->
                if (hasData) {
                    binding.back.visibility = View.VISIBLE
                } else {
                    binding.back.visibility = View.INVISIBLE
                }
            }

        }
        vm.selectResult.observe(this) {
            val adapter = binding.recycler.adapter as MainAdapter
            adapter.submitList(it)
        }

        binding.suggest.bind(vm.allLiveData, this)

        binding.suggest.setOnItemClickListener {
            vm.add2Selected(it) { success ->
                if (success) {
                    binding.edit.setText("")
                }
            }
        }

        vm.snackBarCaller.observe(this) {
            alertSnackBar.setText(it).show()
        }
    }

    private fun initToolBar(info: CrateDefectDelegate.Info) {
        binding.toolbar.title = info.title
//        binding.toolbar.setNavigationIcon(R.drawable.back_ic)
        binding.toolbar.subtitle =
            "${info.year} 年 ${info.monthOfYear} 月 ${info.dayOfMonth} 日  ${info.hourOfDay}:${info.minute} "
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private val alertSnackBar by lazy { Snackbar.make(binding.root, "", Snackbar.LENGTH_LONG) }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)

    }

    override fun onBackPressed() {
        if (binding.back.visibility == View.VISIBLE) {
            binding.back.visibility = View.INVISIBLE
        } else
            super.onBackPressed()
    }
}




