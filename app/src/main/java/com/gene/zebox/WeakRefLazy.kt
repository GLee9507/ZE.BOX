package com.gene.zebox

import java.lang.ref.WeakReference

class WeakRefLazy<T>(private val function: () -> T) : Lazy<T> {
        private var cache: WeakReference<T>? = null
        override val value: T
            get() {
                var t: T? = cache?.get()
                return if (t == null) {
                    t = function()
                    cache = WeakReference(t)
                    t
                } else {
                    t
                }
            }

        override fun isInitialized() = cache?.get() != null
    }