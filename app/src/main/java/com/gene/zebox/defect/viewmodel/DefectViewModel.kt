package com.gene.zebox.defect.viewmodel

import androidx.lifecycle.*
import com.gene.zebox.LetterUtil
import com.gene.zebox.defect.model.DefectItem
import com.gene.zebox.defect.model.DefectModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DefectViewModel : ViewModel() {


    private val model by lazy { DefectModel() }
    val edit = MutableLiveData<String>()
    val queryResult = MutableLiveData<Array<DefectItem>>()
    val selectResult = MutableLiveData<MutableList<DefectItem>>()
    val allLiveData by lazy { model.getLiveData() }
    private val switcher = MutableLiveData<Boolean>()
    val listLiveData = object : MediatorLiveData<MutableList<DefectItem>>() {
        /**
         * if false 使用selectResult else 使用queryResult
         */
        var switcherFlag = false

        init {
            addSource(switcher) {
                value = if (it) {
                    queryResult.value?.toMutableList()
                } else {
                    selectResult.value
                }
                switcherFlag = it
            }

            addSource(queryResult) {
                if (switcherFlag) {
                    value = it.toMutableList()
                }
            }

            addSource(selectResult) {
                if (!switcherFlag) {
                    value = it
                }
            }
        }
    }
    private val ob = Observer<String> {
        if (it.isNullOrEmpty()) {
            switcher.value = false
            return@Observer
        }
        switcher.value = true

        viewModelScope.launch {
            val str = it
            val result = async(Dispatchers.IO) {
                model.queryInput(str)
            }
            queryResult.value = result.await()
        }
    }

    init {
        edit.observeForever(ob)
    }

    override fun onCleared() {
        edit.removeObserver(ob)
    }

    fun add2Selected(index: Int) {
        queryResult.value?.let {
            val list: MutableList<DefectItem> =
                if (selectResult.value == null) ArrayList() else selectResult.value!!
            list.add(it[index])
            selectResult.value = list
        }
    }

    fun newItem() {
        val string = edit.value
        if (string.isNullOrEmpty()) {
            return
        }
        viewModelScope.launch {
            if (string.isNullOrEmpty()) {
                return@launch
            }
            val async = async(Dispatchers.IO) {
                try {
                    val defectItem = DefectItem(
                        string,
                        LetterUtil.getSpells(string)
                    )
                    model.insert(
                        defectItem
                    )
                    return@async defectItem
                } catch (e: Exception) {
                    throw e
                }
            }
            val list: MutableList<DefectItem> =
                if (selectResult.value == null) ArrayList() else selectResult.value!!
            list.add(async.await())
            selectResult.value = list
        }
    }
}