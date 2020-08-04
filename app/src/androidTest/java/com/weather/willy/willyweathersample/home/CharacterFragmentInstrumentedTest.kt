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
import com.weather.willy.willyweathersample.data.ServiceLocator
import com.weather.willy.willyweathersample.data.dummyCharacter
import com.weather.willy.willyweathersample.home.character.CharacterViewModel
import com.weather.willy.willyweathersample.home.character.CharactersFragment
import com.weather.willy.willyweathersample.model.local.Character
import com.weather.willy.willyweathersample.util.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@RunWith(AndroidJUnit4::class)
class CharacterFragmentInstrumentedTest {


    private lateinit var mCharacterViewModel: CharacterViewModel

    private val mCharacters = mutableListOf<Character>()

    private val endIndex = 125
    private val pageSize = 20

    private val mShowProgressLiveData: MutableLiveData<Boolean> = MutableLiveData(false)

    @Before
    fun setup() {
        for (index in 1..endIndex) {
            mCharacters.add(dummyCharacter(index))
        }
        mCharacterViewModel = mock(CharacterViewModel::class.java)
        `when`(mCharacterViewModel.pagedCharacterListLiveData).then { mCharacters.asPagedList() }
        `when`(mCharacterViewModel.showProgress()).then { mShowProgressLiveData }
        ServiceLocator.mFactory = ViewModelUtil.createFor(mCharacterViewModel)
    }


    @Test
    fun characterListFragmentView() {
        launchFragment()
        onView(withId(R.id.rvCharacters)).isVisible()
        onView(withId(R.id.pbCharacterList)).isGone()
    }

    @Test
    fun progressBarVisibilityToggle() {
        launchFragment()
        mShowProgressLiveData.postValue(false)
        onView(withId(R.id.pbCharacterList)).isGone()
        mShowProgressLiveData.postValue(true)
        onView(withId(R.id.pbCharacterList)).isVisible()
    }

    private fun launchFragment() {
        launchFragmentInContainer<CharactersFragment>()
    }
}