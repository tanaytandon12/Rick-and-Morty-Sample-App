package com.weather.willy.willyweathersample.character

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.weather.willy.willyweathersample.util.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

abstract class AbstractViewModelTest {

    @get:Rule
    var mRule = InstantTaskExecutorRule()


    @ExperimentalCoroutinesApi
    @get:Rule
    var mRule2 = TestCoroutineRule()

}