package com.gene.zebox.main.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.gene.zebox.R
import com.gene.zebox.defect.widget.CreateDefectSetDialog
import com.gene.zebox.main.DefectSetViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val dialog by lazy { CreateDefectSetDialog() }
    private val vm by viewModels<DefectSetViewModel>()
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recycler.layoutManager =
            LinearLayoutManager(this)
        recycler.adapter = DefectSetAdapter()
        btn.setOnClickListener {
            //            dialog.show(supportFragmentManager, "asd")
            vm.new()
        }
        vm.data.observe(this) {
            val defectSetAdapter = recycler.adapter as DefectSetAdapter
            defectSetAdapter.submitList(it.toList()) {
            }
        }
    }
}





