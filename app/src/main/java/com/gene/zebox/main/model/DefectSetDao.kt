package com.gene.zebox.main.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DefectSetDao {
    @Delete
    fun delete(vararg defectSet: DefectSet)

    @Insert
    fun insert(vararg defectSet: DefectSet)


    @Update
    fun update(vararg defectSet: DefectSet)

    @Query("select * from defect_set where archive=0 ORDER BY timeout ASC ")
    fun getAllUnarchive(): LiveData<Array<DefectSet>>

    @Query("select * from defect_set where archive=1 ORDER BY createTime ASC")
    fun getAllArchive(): LiveData<Array<DefectSet>>
}