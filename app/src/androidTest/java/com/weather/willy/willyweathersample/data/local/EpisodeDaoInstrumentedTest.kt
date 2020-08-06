package com.weather.willy.willyweathersample.data.local

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.weather.willy.willyweathersample.data.dummyEpisode
import com.weather.willy.willyweathersample.model.local.Episode
import com.weather.willy.willyweathersample.util.getValueForTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EpisodeDaoInstrumentedTest : AbstractDaoInstrumentedTest() {

    private lateinit var mEpisodeDao: EpisodeDao

    override fun setup(database: Database) {
        mEpisodeDao = database.episodeDao()
    }

    override fun setTransactionExecutor(): Boolean = false


    @Test
    fun episodeInsert() {
        val episodeId = 1
        val episode = dummyEpisode(episodeId)
        runBlocking {
            mEpisodeDao.insertEpisode(episode)
        }
        val liveDataValue = mEpisodeDao.fetchEpisode(episodeId)
        assertEquals(liveDataValue.getValueForTest().episodeId, episodeId)
    }

    @Test
    fun episodesInsert() {
        val episodes = mutableListOf<Episode>()
        val startIndex = 1
        val endIndex = 100
        for (index in startIndex..endIndex) {
            episodes.add(dummyEpisode(index))
        }
        runBlocking {
            mEpisodeDao.insertEpisodes(episodes)
        }
        val fetchList = mEpisodeDao.fetchEpisodes()
        assertEquals(fetchList.size, episodes.size)
    }

}