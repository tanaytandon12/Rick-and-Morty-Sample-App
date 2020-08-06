package com.weather.willy.willyweathersample.character

import com.weather.willy.willyweathersample.data.dummyCharacter
import com.weather.willy.willyweathersample.data.repository.CharacterRepository
import com.weather.willy.willyweathersample.util.getValueForTesting
import com.weather.willy.willyweathersample.home.character.CharacterViewModel
import com.weather.willy.willyweathersample.util.fakes.CharacterRepoFake
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


class CharacterViewModelTest : AbstractViewModelTest() {


    private lateinit var mCharacterRepository: CharacterRepository
    private lateinit var mCharacterViewModel: CharacterViewModel


    @Before
    fun setup() {
        mCharacterRepository = CharacterRepoFake()
        mCharacterViewModel = CharacterViewModel(
            mCharacterRepository
        )
    }

    private fun showError(value: Boolean) {
        (mCharacterRepository as CharacterRepoFake).throwError = value
    }

    @ExperimentalCoroutinesApi
    @Test
    fun showProgressOnError() {
        mRule2.runBlockingTest {
            showError(true)
            mCharacterViewModel.fetchCharacters()
            assertFalse(mCharacterViewModel.showProgress().getValueForTesting())
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun showProgressOnSuccess() {
        mRule2.runBlockingTest {
            showError(false)
            mCharacterViewModel.fetchCharacters()
            assertFalse(mCharacterViewModel.showProgress().getValueForTesting())
        }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun onCharacterSelected() {
        mRule2.runBlockingTest {
            val characterId = 1
            val dummyCharacter = dummyCharacter(characterId)
            mCharacterViewModel.onCharacterSelected(character = dummyCharacter)
            assertEquals(
                mCharacterViewModel.selectedCharacterLiveData()
                    .getValueForTesting().character.characterId,
                characterId
            )
            assertTrue(mCharacterViewModel.navigateOnCharacterSelected().getValueForTesting())
        }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun paginatedData() {
        mRule2.runBlockingTest {
            assertNotNull(mCharacterViewModel.pagedCharacterListLiveData)
        }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun resetNavigationOnCharacterSelected() {
        mRule2.runBlockingTest {
            mCharacterViewModel.resetNavigateOnCharacterSelected()
            assertFalse(mCharacterViewModel.navigateOnCharacterSelected().getValueForTesting())
        }
    }
}