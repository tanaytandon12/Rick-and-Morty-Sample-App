package com.weather.willy.willyweathersample.home

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.weather.willy.willyweathersample.R
import com.weather.willy.willyweathersample.home.character.CharactersFragment
import com.weather.willy.willyweathersample.util.isGone
import com.weather.willy.willyweathersample.util.isVisible
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CharacterFragmentInstrumentedTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()



    @Test
    fun characterListFragmentView() {
        val scenario = launchFragmentInContainer<CharactersFragment>()
        onView(withId(R.id.rvCharacters)).isVisible()
        onView(withId(R.id.pbCharacterList)).isGone()
    }
}