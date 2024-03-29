package com.gene.zebox.defect.model

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery


@Dao
interface DefectItemDao {
    @Delete
    fun delete(vararg defectItem: DefectItem)

    @Insert
    fun insert(vararg defectItem: DefectItem)

    @Update
    fun update(vararg defectItem: DefectItem)

    @RawQuery
    fun query(query: SupportSQLiteQuery): Array<DefectItem>

    @Query("SELECT * FROM defect_item WHERE id IN(:ids)")
    fun query(ids: Array<Long>): Array<DefectItem>

    @Query("SELECT * FROM defect_item ")
    fun query(): LiveData<Array<DefectItem>>
//    // Usage of RawDao
//    SimpleSQLiteQuery query = new SimpleSQLiteQuery(
//    "SELECT * FROM Song WHERE id = ? LIMIT 1",
//    new Object[]{ songId});
//    Song song = rawDao.getSongViaQuery(query);
}

