package com.gene.zebox

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gene.zebox.defect.model.DefectDao
import com.gene.zebox.defect.model.DefectItem

// Song and Album are classes annotated with @Entity.
@Database(version = 1, entities = [DefectItem::class])
abstract class MusicDatabase : RoomDatabase() {
    // SongDao is a class annotated with @Dao.
    abstract val defectDao: DefectDao
}