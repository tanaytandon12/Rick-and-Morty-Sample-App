package com.weather.willy.willyweathersample.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.weather.willy.willyweathersample.data.inMemoryDatabase
import com.weather.willy.willyweathersample.data.local.Database
import com.weather.willy.willyweathersample.data.network.NetworkAPI
import com.weather.willy.willyweathersample.data.NetworkInterceptor
import com.weather.willy.willyweathersample.data.networkAPIForTest
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.ExpectedException

abstract class AbstractRepositoryTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val exceptionRule = ExpectedException.none()

    private lateinit var mDatabase: Database
    private lateinit var mNetworkAPI: NetworkAPI
    private val networkInterceptor =
        NetworkInterceptor()

    @Before
    fun setup() {
        mDatabase =
            inMemoryDatabase(ApplicationProvider.getApplicationContext(), setTransactionExecutor())
        mNetworkAPI = networkAPIForTest(networkInterceptor)
        setup(mDatabase, mNetworkAPI)
    }

    @After
    fun tearDown() {
        mDatabase.close()
    }

    fun setGenerateError(value: Boolean) {
        networkInterceptor.mGenerateRandomError = value
    }

    fun <T> setResult(data: T) {
        networkInterceptor.setData(data)
    }

    abstract fun setTransactionExecutor(): Boolean

    abstract fun setup(database: Database, networkAPI: NetworkAPI)
}