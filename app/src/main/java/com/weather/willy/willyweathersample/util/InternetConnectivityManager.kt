package com.weather.willy.willyweathersample.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object InternetConnectivityManager {

    private lateinit var mContext: Context

    fun init(context: Context){
        mContext = context.applicationContext
    }

    @SuppressLint( "NewApi")
    fun isInternetConnected(): Boolean {
        return if (deviceOsGreaterThan(28)) {
            val connectivityManager = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkCapability = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            activeNetworkCapability?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true ||
                    activeNetworkCapability?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true
                    || activeNetworkCapability?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) == true
        } else {
            val connectivityManager = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.activeNetworkInfo?.isConnected == true
        }
    }
}