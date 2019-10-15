package com.gene.zebox.main

import androidx.lifecycle.ViewModel
import com.gene.zebox.main.model.DefectSet
import com.gene.zebox.main.model.DefectSetModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DefectSetViewModel : ViewModel() {
    private val model by lazy { DefectSetModel() }
    val data = model.allUnarchive
    var i = 0
    fun new() {
        CoroutineScope(Dispatchers.IO).launch {

            model.new(
                DefectSet(
                    "李季",
                    (++i).toString(),
                    "001",
                    System.currentTimeMillis(),
                    System.currentTimeMillis()
                )
            )
        }
    }
}