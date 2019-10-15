package com.gene.zebox.main.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.gene.zebox.DB
import com.gene.zebox.defect.model.DefectItem


@Entity(tableName = "defect_set")
class DefectSet(
    var builder: String = "",
    var location: String = "",
    var number: String = "",
    var timeout: Long = -1,//超期时间
    @PrimaryKey
    var createTime: Long = -1,//创建时间
    var defects: List<DefectItem> = ArrayList(),

    var archive: Boolean = false
)

class DefectIdsConverters {
    @TypeConverter
    fun fromIdsStr(value: String?): List<DefectItem>? {
        return value?.let {
            ArrayList<DefectItem>().apply {
                val split = it.split(',')
                val ids = ArrayList<Long>().apply {
                    for (s in split) {
                        if (s.isEmpty()) continue
                        try {
                            add(s.toLong())
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }.toTypedArray()
                addAll(DB.defectItemDao.query(ids))

            }
        }
    }

    @TypeConverter
    fun idsSetToIdsString(value: List<DefectItem>?): String? {
        return value?.let {
            StringBuilder().apply {
                for (l in it) {
                    append(l.id.toString()).append(',')
                }
                dropLastWhile {
                    it == ','
                }
            }.toString()
        }
    }
}