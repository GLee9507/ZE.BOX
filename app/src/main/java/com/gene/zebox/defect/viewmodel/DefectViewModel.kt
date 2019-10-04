package com.gene.zebox.defect.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gene.zebox.App.Companion.EXCEPTION_HANDLER
import com.gene.zebox.LetterUtil
import com.gene.zebox.defect.model.DefectItem
import com.gene.zebox.defect.model.DefectModel
import com.gene.zebox.defect.widget.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DefectViewModel : ViewModel() {

    private val model by lazy { DefectModel() }
    val edit = MutableLiveData<String>()
    val selectResult = MutableLiveData<MutableList<DefectItem>?>()
    val allLiveData by lazy { model.getLiveData() }
    val snackBarCaller by lazy { LiveEvent<String>() }

    fun add2Selected(defectItem: DefectItem, block: (Boolean) -> Unit) {

        viewModelScope.launch(Dispatchers.IO) {
            val value = selectResult.value
            val find = value?.find {
                return@find it.text == defectItem.text
            }
            if (find != null) {
                withContext(Dispatchers.Main) {
                    block.invoke(false)
                    snackBarCaller.value = "不可重复添加相同缺陷哦"
                }
                return@launch
            }
            val list: MutableList<DefectItem> =
                if (selectResult.value == null) ArrayList() else ArrayList(selectResult.value!!)
            list.add(defectItem)
            withContext(Dispatchers.Main) {
                selectResult.value = list
                block.invoke(true)
            }
            model.updateCountAndTime(defectItem)
        }
    }

    fun newItem() {
        val string = edit.value
        if (string.isNullOrEmpty()) {
            return
        }
        viewModelScope.launch(EXCEPTION_HANDLER) {
            if (string.isNullOrEmpty()) {
                return@launch
            }
            val async = async(Dispatchers.IO + EXCEPTION_HANDLER) {
                val defectItem = DefectItem(
                    string,
                    LetterUtil.getSpells(string)
                )
                try {
                    model.insert(
                        defectItem
                    )
                } catch (e: Exception) {
                    throw e
                }
                return@async defectItem
            }
            val list: MutableList<DefectItem> =
                if (selectResult.value == null) ArrayList() else selectResult.value!!
            list.add(async.await())
            selectResult.value = list
        }
    }
}