package com.weather.willy.willyweathersample.network

import com.weather.willy.willyweathersample.data.NetworkInterceptor
import com.weather.willy.willyweathersample.data.ServiceLocator
import com.weather.willy.willyweathersample.data.network.NetworkAPI
import com.weather.willy.willyweathersample.data.networkAPIForTest
import org.junit.Before


abstract class AbstractNetworkAPITest() {

    @Before
    fun setup() {
        val networkAPI = ServiceLocator.getNetworkAPI()
        setup(networkAPI)
    }

    abstract fun setup(networkAPI: NetworkAPI)

    fun setGenerateError(value: Boolean) {
        ServiceLocator.setGenerateRandomError(value)
    }

    fun <T> setResult(data: T) {
        ServiceLocator.setData(data)
    }
}


