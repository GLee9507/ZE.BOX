package com.gene.zebox.main.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.gene.zebox.defect.model.DefectItem

@Entity(tableName = "defect_set")
data class DefectSet(
    val
    val location: String,
    val number: String,
    val timeout: Long,//超期时间
    @PrimaryKey
    val createTime: Long,//创建时间
    @Ignore
    val defects: List<DefectItem> = ArrayList(),
    val archive: Boolean = false

) {
}