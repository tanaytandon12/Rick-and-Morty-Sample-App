package com.weather.willy.willyweathersample.data.local.di

import android.content.Context
import androidx.room.Room
import com.weather.willy.willyweathersample.BuildConfig
import com.weather.willy.willyweathersample.data.local.Database


fun Any.databaseInstance(context: Context): Database =
    Room.databaseBuilder(context, Database::class.java, BuildConfig.DATABASE_NAME).build()

