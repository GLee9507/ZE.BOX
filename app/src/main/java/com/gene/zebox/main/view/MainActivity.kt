package com.gene.zebox.main.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gene.zebox.R
import com.gene.zebox.defect.view.DefectActivity
import com.gene.zebox.defect.widget.CrateDefectDelegate
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val crateDefectDelegate by lazy { CrateDefectDelegate(this) }

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn.setOnClickListener {
            crateDefectDelegate.start {
                startActivity(Intent(this, DefectActivity::class.java).apply {
                    putExtra("info", it)
                })
            }
        }

    }


}





