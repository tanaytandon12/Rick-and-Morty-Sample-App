package com.weather.willy.willyweathersample.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

fun <T> LiveData<T>.getValueForTest(): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(t: T) {
            data = t
            latch.countDown()
            this@getValueForTest.removeObserver(this)
        }
    }


    this.observeForever(observer)
    if (!latch.await(2, TimeUnit.SECONDS)) {
        this.removeObserver(observer)
    }

    return data as T
}