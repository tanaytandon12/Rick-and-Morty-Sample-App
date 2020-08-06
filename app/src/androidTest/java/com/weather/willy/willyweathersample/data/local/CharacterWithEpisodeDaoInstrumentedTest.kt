package com.weather.willy.willyweathersample.data.local

import com.weather.willy.willyweathersample.data.dummyCharacter
import com.weather.willy.willyweathersample.model.local.CharacterEpisodeRelation
import com.weather.willy.willyweathersample.model.local.Episode
import com.weather.willy.willyweathersample.util.getValueForTest
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class CharacterWithEpisodeDaoInstrumentedTest : AbstractDaoInstrumentedTest() {

    private lateinit var mCharacterWithEpisodeDao: CharacterWithEpisodeDao
    private lateinit var mCharacterDao: CharacterDao
    private lateinit var mEpisodeDao: EpisodeDao

    override fun setup(database: Database) {
        mCharacterWithEpisodeDao = database.characterWithEpisodeDao()
        mCharacterDao = database.characterDao()
        mEpisodeDao = database.episodeDao()
    }

    override fun setTransactionExecutor(): Boolean = true


    @Test
    fun insertCharacterEpisodeRelation() {
        val list = mutableListOf<CharacterEpisodeRelation>()
        val startCharacterId = 1
        val endCharacterId = 10
        val endEpisodeId = 10
        for (characterId in startCharacterId..endCharacterId) {
            for (episodeId in characterId..endEpisodeId) {
                list.add(CharacterEpisodeRelation(episodeId, characterId))
            }
        }
        runBlocking {
            mCharacterWithEpisodeDao.insertCharacterEpisodeRelation(list)
        }
        assertEquals(mCharacterWithEpisodeDao.fetchAll().size, list.size)

        runBlocking {
            mCharacterWithEpisodeDao.insertCharacterEpisodeRelation(list)
        }
        // check to ensure that no duplicate entries are made
        assertEquals(mCharacterWithEpisodeDao.fetchAll().size, list.size)
    }

    @Test
    fun characterWithEpisode() {
        val characterId = 11
        val startEpisodeId = 11
        val endEpisodeId = 26
        val dummyCharacter = dummyCharacter(characterId)
        val episodes = mutableListOf<Episode>()
        val relations = mutableListOf<CharacterEpisodeRelation>()
        for (index in startEpisodeId..endEpisodeId) {
            episodes.add(Episode(episodeId = index))
            relations.add(CharacterEpisodeRelation(episodeId = index, characterId = characterId))
        }
        runBlocking {
            mCharacterWithEpisodeDao.save(
                listOf(dummyCharacter),
                episodes, relations
            )
        }

        val characterWithEpisode =
            mCharacterWithEpisodeDao.characterWithEpisodes(characterId).getValueForTest()
        assertEquals(characterWithEpisode.character.characterId, characterId)
        assertEquals(characterWithEpisode.episodes.size, episodes.size)
    }

}