package com.gene.zebox.defect.model

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.gene.zebox.DB
import kotlin.concurrent.read
import kotlin.concurrent.write

class DefectModel {
    private val dao by lazy { DB.defectItemDao }


    fun queryInput(str: String): Array<DefectItem> {
        val sql = StringBuilder(
            "SELECT * FROM defect_item WHERE text LIKE '%$str%' OR ("
        )
        str.forEachIndexed { index, c ->
            sql.append("text LIKE ")
                .append("'%")
                .append(c)
                .append("%'")
            if (index != str.lastIndex) {
                sql.append(" AND ")
            } else {
                sql.append(" )")
            }
        }
        sql.append(" ORDER BY count DESC")


            return try {
                dao.query(SimpleSQLiteQuery(sql.toString()))
            } catch (e: Exception) {
                throw e
            }

    }

    fun getLiveData(): LiveData<Array<DefectItem>> =  dao.query()

    fun delete(vararg defectItem: DefectItem) {
        dao.delete(*defectItem)
    }

    fun insert(vararg defectItem: DefectItem) {
       dao.insert(*defectItem)
    }

    fun updateCountAndTime(vararg defectItem: DefectItem) {
        defectItem.forEach {
            it.count = it.count + 1
            it.timestamp = System.currentTimeMillis()
        }
        dao.update(*defectItem)
    }
}