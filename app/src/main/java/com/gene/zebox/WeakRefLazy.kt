package com.gene.zebox

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import java.lang.ref.WeakReference

class WeakRefLazy<T>(
    private val lifecycleOwner: LifecycleOwner? = null,
    private val function: () -> T
) :
    Lazy<T>, LifecycleObserver {
    init {
        lifecycleOwner?.lifecycle?.addObserver(this)
    }

    private var cache: WeakReference<T>? = null
    private var t: T? = null
    override val value: T
        get() {
            var t: T? = cache?.get()
            return if (t == null) {
                t = function()
                cache = WeakReference(t)
                t
            } else {
                t
            }.apply { this@WeakRefLazy.t = t }
        }

    override fun isInitialized() = cache?.get() != null

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        lifecycleOwner?.lifecycle?.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        this.t = null
        lifecycleOwner?.lifecycle?.removeObserver(this)
    }
}