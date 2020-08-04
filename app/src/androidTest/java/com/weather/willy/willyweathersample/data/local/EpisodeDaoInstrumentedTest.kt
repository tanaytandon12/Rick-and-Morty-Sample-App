package com.weather.willy.willyweathersample.data.local

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.weather.willy.willyweathersample.data.dummyEpisode
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

    @Test
    fun episodeInsert() {
        val episodeId = 1
        val episode = dummyEpisode(episodeId)
        runBlocking {
            mEpisodeDao.insertEpisode(episode)
        }
        val liveDataValue = mEpisodeDao.fetchEpisode(episodeId)
        assertEquals(liveDataValue.getValueForTest().id, episodeId)
    }


}