package com.gene.zebox.main.model

import android.database.sqlite.SQLiteException
import com.gene.zebox.DB

class DefectSetModel {
    private val defectSetDao by lazy { DB.defectSetDao }
    private val defectItemDao by lazy { DB.defectItemDao }


    val allUnarchive by lazy {
        defectSetDao.getAllUnarchive()
    }

    val allArchive by lazy { defectSetDao.getAllArchive() }

    @Throws(SQLiteException::class)
    fun new(defectSet: DefectSet) {
        defectSetDao.insert(defectSet)
    }

    @Throws(SQLiteException::class)
    fun archive(defectSet: DefectSet) {
        defectSetDao.update(defectSet.apply { this.archive = true })
    }
}