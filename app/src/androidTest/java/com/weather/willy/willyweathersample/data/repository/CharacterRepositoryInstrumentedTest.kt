package com.weather.willy.willyweathersample.data.repository

import androidx.room.paging.LimitOffsetDataSource
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.weather.willy.willyweathersample.data.local.CharacterDao
import com.weather.willy.willyweathersample.data.local.Database
import com.weather.willy.willyweathersample.data.dummyCharacter
import com.weather.willy.willyweathersample.data.network.CharacterApiImpl
import com.weather.willy.willyweathersample.data.network.NetworkAPI
import com.weather.willy.willyweathersample.model.local.Character
import com.weather.willy.willyweathersample.model.network.CharacterResponse
import com.weather.willy.willyweathersample.model.network.CharacterResponseWrapper
import com.weather.willy.willyweathersample.data.dummyCharacterResponse
import com.weather.willy.willyweathersample.model.CharacterListLoadingError
import com.weather.willy.willyweathersample.util.getValueForTest
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*


@RunWith(AndroidJUnit4::class)
class CharacterRepositoryInstrumentedTest : AbstractRepositoryTest() {

    private lateinit var mCharacterRepository: CharacterRepository

    private lateinit var mCharacterDao: CharacterDao

    override fun setup(database: Database, networkAPI: NetworkAPI) {
        mCharacterDao = database.characterDao()
        mCharacterRepository = CharacterRepositoryImpl(
            CharacterApiImpl(networkAPI), mCharacterDao
        )
    }

    @Test
    fun pagedCharacterList() {
        mCharacterDao.deleteAllCharacters()
        var dataSource = (mCharacterRepository.paginatedCharacterListDataSource()
            .create() as LimitOffsetDataSource<Character>)
        assertNotNull(dataSource)
        val list = mutableListOf<Character>()
        val pageSize = 125
        for (index in 1..pageSize) {
            list.add(dummyCharacter(index))
        }
        runBlocking {
            mCharacterDao.saveCharacterList(list)
        }
        dataSource = (mCharacterRepository.paginatedCharacterListDataSource()
            .create() as LimitOffsetDataSource<Character>)
        assertEquals(dataSource.loadRange(0, pageSize).size, pageSize)
    }


    @Test
    fun characterListInsert() {
        mCharacterDao.deleteAllCharacters()
        val characters = mutableListOf<CharacterResponse>(
            dummyCharacterResponse(1, 1, 11),
            dummyCharacterResponse(2, 2, 12),
            dummyCharacterResponse(3, 1, 121),
            dummyCharacterResponse(4, 1, 15)
        )
        val response = CharacterResponseWrapper(characters)
        setGenerateError(false)
        setResult(response)
        runBlocking {
            mCharacterRepository.fetchCharacterList()
        }
        assertEquals(mCharacterDao.fetchCharacterList().getValueForTest().size, characters.size)
    }

    @Test
    fun characterListInsertException() {
        mCharacterDao.deleteAllCharacters()
        setGenerateError(true)
        exceptionRule.expect(CharacterListLoadingError::class.java)
        runBlocking {
            mCharacterRepository.fetchCharacterList()
        }
    }

    @Test
    fun characterByIdFetch() {
        val characterId = 12
        val dummy = dummyCharacter(characterId)
        runBlocking {
            mCharacterDao.saveCharacterList(listOf(dummy))
        }
        val value = mCharacterRepository.fetchCharacterById(characterId).getValueForTest()
        assertEquals(value.id, characterId)
    }
}