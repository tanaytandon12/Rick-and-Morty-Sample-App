package com.weather.willy.willyweathersample.util

import android.os.Build
import com.weather.willy.willyweathersample.model.local.Character
import retrofit2.Response

fun <T> Response<T>.requestSuccess() = this.isSuccessful && body() != null
fun Any.deviceOsGreaterThan(sdkInt: Int) = Build.VERSION.SDK_INT > sdkInt

fun Any.deviceOsLessThan(sdkInt: Int) = Build.VERSION.SDK_INT < sdkInt
