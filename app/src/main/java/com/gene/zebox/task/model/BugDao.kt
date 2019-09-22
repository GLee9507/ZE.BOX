package com.gene.zebox.task.model

import android.database.sqlite.SQLiteException
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.RoomDatabase
import androidx.room.Database


@Dao
interface BugDao {
    @Delete
    fun delete(bugItem: BugItem)

    @Throws(SQLiteException::class)
    @Insert
    fun insert(vararg bugItem: BugItem)

    @RawQuery
    fun query(query: RoomSQLiteQuery): Array<BugItem>

    @Query("SELECT * FROM bug_item ")
    fun query(): LiveData<Array<BugItem>>
//    // Usage of RawDao
//    SimpleSQLiteQuery query = new SimpleSQLiteQuery(
//    "SELECT * FROM Song WHERE id = ? LIMIT 1",
//    new Object[]{ songId});
//    Song song = rawDao.getSongViaQuery(query);
}

