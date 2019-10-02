package com.gene.zebox.defect.model

import androidx.lifecycle.LiveData
import androidx.room.RoomSQLiteQuery
import com.gene.zebox.App
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

class DefectModel {
    private val dao by lazy { App.DB.defectDao }
    private val lock by lazy { ReentrantReadWriteLock(true) }


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
        val query = RoomSQLiteQuery.acquire(sql.toString(), 0)
        return lock.read {
            return try {
                dao.query(query)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    fun getLiveData(): LiveData<Array<DefectItem>> = lock.read { dao.query() }

    fun delete(vararg defectItem: DefectItem) {
        lock.write { dao.delete(*defectItem) }
    }

    fun insert(vararg defectItem: DefectItem) {
        lock.write { dao.insert(*defectItem) }
    }

    fun updateCountAndTime(vararg defectItem: DefectItem) {
        defectItem.forEach {
            it.count = it.count + 1
            it.timestamp = System.currentTimeMillis()
        }
        lock.write { dao.update(*defectItem) }
    }
}