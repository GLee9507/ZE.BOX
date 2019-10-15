package com.gene.zebox

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.EmptyCoroutineContext


class App : Application() {


    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        CONTEXT = this
    }
}

