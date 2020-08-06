package com.weather.willy.willyweathersample.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.weather.willy.willyweathersample.data.inMemoryDatabase
import org.junit.After
import org.junit.Before
import org.junit.Rule


abstract class AbstractDaoInstrumentedTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var mDatabase: Database

    @Before
    fun setup() {
        mDatabase =
            inMemoryDatabase(ApplicationProvider.getApplicationContext(), setTransactionExecutor())
        setup(mDatabase)
    }

    @After
    fun tearDown() {
        mDatabase.close()
    }

    abstract fun setTransactionExecutor(): Boolean

    abstract fun setup(database: Database)
}

