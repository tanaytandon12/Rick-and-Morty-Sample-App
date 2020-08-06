package com.weather.willy.willyweathersample.home

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.weather.willy.willyweathersample.R
import com.weather.willy.willyweathersample.data.ServiceLocator
import com.weather.willy.willyweathersample.data.dummyCharacter
import com.weather.willy.willyweathersample.home.character.CharacterAdapter
import com.weather.willy.willyweathersample.home.character.CharacterViewModel
import com.weather.willy.willyweathersample.home.character.CharactersFragment
import com.weather.willy.willyweathersample.model.local.Character
import com.weather.willy.willyweathersample.util.*
import org.hamcrest.Matchers.greaterThan
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@RunWith(AndroidJUnit4::class)
class CharacterFragmentInstrumentedTest {


    private lateinit var mCharacterViewModel: CharacterViewModel

    private val mCharacters = mutableListOf<Character>()

    private val endIndex = 125
    private val pageSize = 20

    private val mShowProgressLiveData: MutableLiveData<Boolean> = MutableLiveData(false)
    private var mNavigateLiveData: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>(false)

    @Before
    fun setup() {
        for (index in 1..endIndex) {
            mCharacters.add(dummyCharacter(index))
        }
        mCharacterViewModel = mock(CharacterViewModel::class.java)
        val pagedList = mCharacters.asPagedList(pageSize)
        `when`(mCharacterViewModel.pagedCharacterListLiveData).then {
            pagedList
        }
        `when`(mCharacterViewModel.navigateOnCharacterSelected()).then { mNavigateLiveData }
        `when`(mCharacterViewModel.showProgress()).then { mShowProgressLiveData }
        ServiceLocator.mFactory = ViewModelUtil.createFor(mCharacterViewModel)
    }

    @After
    fun teardown() {
        ServiceLocator.mFactory = null
    }


    @Test
    fun characterListFragmentView() {
        launchFragment()
        onView(withId(R.id.rvCharacters)).visibilityIsVisible()
        onView(withId(R.id.pbCharacterList)).visibilityIsGone()
    }

    @Test
    fun progressBarVisibilityToggle() {
        launchFragment()
        mShowProgressLiveData.postValue(false)
        onView(withId(R.id.pbCharacterList)).visibilityIsGone()
        mShowProgressLiveData.postValue(true)
        onView(withId(R.id.pbCharacterList)).visibilityIsVisible()
    }

    @Test
    fun recyclerviewSize() {
        launchFragment()
        onView(withId(R.id.rvCharacters)).visibilityIsVisible()


        onView(withId(R.id.rvCharacters)).perform(waitUnit(hasItemCount(greaterThan(0))))

        onView(RecyclerViewItemMatcher(R.id.rvCharacters).atPosition(0, R.id.tvName)).check(
            matches(
                withText(mCharacters[0].name)
            )
        )
        onView(withId(R.id.rvCharacters)).perform(
            RecyclerViewActions.scrollToPosition<CharacterAdapter.CharacterViewHolder>(
                5
            )
        )
        onView(RecyclerViewItemMatcher(R.id.rvCharacters).atPosition(5, R.id.tvName)).check(
            matches(
                withText(mCharacters[5].name)
            )
        )

        onView(withId(R.id.rvCharacters)).perform(
            RecyclerViewActions.scrollToPosition<CharacterAdapter.CharacterViewHolder>(
                10
            )
        )
        onView(RecyclerViewItemMatcher(R.id.rvCharacters).atPosition(10, R.id.tvName)).check(
            matches(
                withText(mCharacters[10].name)
            )
        )
    }

    private fun launchFragment() {
        launchFragmentInContainer<CharactersFragment>()
    }
}