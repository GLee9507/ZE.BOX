package com.gene.zebox.defect.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gene.zebox.App.Companion.EXCEPTION_HANDLER
import com.gene.zebox.LetterUtil
import com.gene.zebox.defect.model.DefectItem
import com.gene.zebox.defect.model.DefectModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DefectViewModel : ViewModel() {
    companion object {
        const val QUERY = 100
    }

    private val model by lazy { DefectModel() }
    val edit = MutableLiveData<String>()
    val queryResult = MutableLiveData<Array<DefectItem>?>()
    val selectResult = MutableLiveData<MutableList<DefectItem>?>()
    val allLiveData by lazy { model.getLiveData() }
    private val handler by lazy {
        Handler(Looper.getMainLooper()) {
            return@Handler if (it.what == QUERY) {
                val string = it.obj as String?
                if (string.isNullOrEmpty()) {
                    queryResult.value = null
                    return@Handler true
                }
                viewModelScope.launch {
                    val result = async(Dispatchers.IO) {
                        model.queryInput(string)
                    }
                    queryResult.value = result.await()
                }
                true
            } else false

        }
    }
    private val ob = Observer<String> {
        handler.removeMessages(QUERY)
        val message = handler.obtainMessage(QUERY, it)
        handler.sendMessageDelayed(message, 256)
    }

    init {
        edit.observeForever(ob)
    }

    override fun onCleared() {
        edit.removeObserver(ob)
    }

    fun add2Selected(defectItem: DefectItem) {
        val list: MutableList<DefectItem> =
            if (selectResult.value == null) ArrayList() else selectResult.value!!
        list.add(defectItem)
        selectResult.value = list
        viewModelScope.launch(Dispatchers.IO) {
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