package com.gene.zebox

import android.app.Application
import android.os.Build
import androidx.room.Room
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.locks.ReentrantReadWriteLock

public val DATA_FORMAT by WeakRefLazy {
    SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss",
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            CONTEXT.resources.configuration.locales[0]
        } else {
            CONTEXT.resources.configuration.locale
        }
    ).apply {
        timeZone = TimeZone.getTimeZone("GMT")
    }
}

lateinit var CONTEXT: Application

val DB by lazy {
    Room.databaseBuilder(CONTEXT, DefectDatabase::class.java, "database-defect")
        .build()
}

// val LOCK by lazy { ReentrantReadWriteLock(true) }

