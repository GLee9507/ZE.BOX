package com.gene.zebox

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gene.zebox.defect.model.DefectItem
import com.gene.zebox.defect.model.DefectItemDao
import com.gene.zebox.main.model.DefectIdsConverters
import com.gene.zebox.main.model.DefectSet
import com.gene.zebox.main.model.DefectSetDao

// Song and Album are classes annotated with @Entity.
@Database(version = 1, entities = [DefectItem::class, DefectSet::class])
@TypeConverters(DefectIdsConverters::class)
abstract class DefectDatabase : RoomDatabase() {
    // SongDao is a class annotated with @Dao.
    abstract val defectItemDao: DefectItemDao
    abstract val defectSetDao: DefectSetDao
}