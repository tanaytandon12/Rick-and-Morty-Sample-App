package com.weather.willy.willyweathersample

import android.app.Application
import com.weather.willy.willyweathersample.data.ServiceLocator
import com.weather.willy.willyweathersample.util.InternetConnectivityManager

class RickAndMortyApp : Application() {

    companion object {
        lateinit var INSTANCE: RickAndMortyApp
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        ServiceLocator.init(this)
        InternetConnectivityManager.init(this)
    }
}