package com.gene.zebox.main.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gene.zebox.defect.view.DefectActivity
import com.gene.zebox.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn.setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    DefectActivity::class.java
                )
            )
        }

    }


}





