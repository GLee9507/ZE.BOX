package com.gene.zebox

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gene.zebox.main.model.TaskItem
import com.gene.zebox.task.model.BugDao
import com.gene.zebox.task.model.BugItem

// Song and Album are classes annotated with @Entity.
@Database(version = 1, entities = [BugItem::class, TaskItem::class])
abstract class MusicDatabase : RoomDatabase() {
    // SongDao is a class annotated with @Dao.
    abstract val bugDao: BugDao
}