package com.weather.willy.willyweathersample.data.local

import androidx.room.paging.LimitOffsetDataSource
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.weather.willy.willyweathersample.data.dummyCharacter
import com.weather.willy.willyweathersample.model.local.Character
import kotlinx.coroutines.runBlocking
import com.weather.willy.willyweathersample.util.getValueForTest
import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class CharacterDaoInstrumentedTest : AbstractDaoInstrumentedTest() {


    private lateinit var characterDao: CharacterDao

    override fun setup(database: Database) {
        characterDao = database.characterDao()
    }

    @Test
    fun characterDaoMultipleInsert() {
        val startIndex = 1
        val endIndex = 15
        val pageSize = 10
        val list = mutableListOf<Character>()
        for (index in startIndex..endIndex) {
            list.add(dummyCharacter(index))
        }
        runBlocking {
            characterDao.saveCharacterList(list)
        }
        assertEquals(characterDao.fetchCharacterList().getValueForTest().size, endIndex)
        assertEquals(
            (characterDao.fetchPagedCharacterList().create() as LimitOffsetDataSource).loadRange(
                0,
                pageSize
            ).size, pageSize
        )
    }

    @Test
    fun characterDaoCharacterFetchById() {
        val characterId = 1
        val character = dummyCharacter(characterId)
        runBlocking {
            characterDao.saveCharacterList(listOf(character))
        }
        val characterValue = characterDao.fetchCharacter(characterId).getValueForTest()
        assertEquals(characterValue.id, characterId)
    }
}