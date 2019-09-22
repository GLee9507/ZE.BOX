package com.gene.zebox

import android.app.Application
import android.content.Context
import androidx.room.Room


class App : Application() {
    companion object {
        lateinit var context: Application
        val DB by lazy {
            Room
                .databaseBuilder(context, MusicDatabase::class.java, "database-name")
                .build()
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        context = this

    }
}

