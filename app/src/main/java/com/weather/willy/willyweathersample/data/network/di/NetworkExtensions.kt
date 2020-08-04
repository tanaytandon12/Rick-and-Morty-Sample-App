package com.weather.willy.willyweathersample.data.network.di

import com.weather.willy.willyweathersample.BuildConfig
import com.weather.willy.willyweathersample.data.network.NetworkAPI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

fun Any.networkAPI(): NetworkAPI = Retrofit.Builder().baseUrl(BuildConfig.SERVER_URL)
    .client(OkHttpClient.Builder().apply {
        addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
    }.build()).build().create(NetworkAPI::class.java)
