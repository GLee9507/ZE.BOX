package com.gene.zebox

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.EmptyCoroutineContext


class App : Application() {
    companion object {
        lateinit var CONTEXT: Application
        val DB by lazy {
            Room
                .databaseBuilder(CONTEXT, MusicDatabase::class.java, "database-name")
                .build()
        }
        val EXCEPTION_HANDLER by lazy {
            CoroutineExceptionHandler { _, exception ->
                Log.e("gene","Caught $exception")
            }
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        CONTEXT = this
    }
}

