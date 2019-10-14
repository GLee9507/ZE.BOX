package com.gene.zebox.defect.view

import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.gene.zebox.R
import com.gene.zebox.databinding.ActivityNewTaskBinding
import com.gene.zebox.defect.viewmodel.DefectViewModel
import com.gene.zebox.defect.widget.CrateDefectDelegate
import com.gene.zebox.defect.widget.MainAdapter
import com.gene.zebox.defect.widget.SuggestDialog
import com.google.android.material.snackbar.Snackbar


class DefectActivity : AppCompatActivity() {

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityNewTaskBinding>(
            this,
            R.layout.activity_new_task
        )
    }

    private val vm: DefectViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val info = intent.getParcelableExtra<CrateDefectDelegate.Info>("info")
        initToolBar(info ?: CrateDefectDelegate.Info())
        binding.lifecycleOwner = this
        binding.vm = vm
        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.adapter = MainAdapter()
        binding.fabBottomAppbar.setOnClickListener {
            suggestDialog.reset()
            suggestDialog.show(supportFragmentManager, SuggestDialog.TAG)
        }

        vm.selectResult.observe(this) {
            val adapter = binding.recycler.adapter as MainAdapter
            adapter.submitList(it)
        }


        vm.snackBarCaller.observe(this) {
            alertSnackBar.setText(it).show()
        }

    }


    private fun initToolBar(info: CrateDefectDelegate.Info) {
        binding.toolbar.title = info.title
        binding.toolbar.subtitle =
            "${info.year} 年 ${info.monthOfYear} 月 ${info.dayOfMonth} 日  ${info.hourOfDay}:${info.minute} "
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.toolbar.setOnMenuItemClickListener {
            binding.fabBottomAppbar.performClick()
        }
    }


    private val suggestDialog by lazy {
        SuggestDialog()
    }
    private val alertSnackBar by lazy {
        Snackbar.make(binding.root, "", Snackbar.LENGTH_LONG).apply { }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onBackPressed() {
        super.onBackPressed()
    }
}




