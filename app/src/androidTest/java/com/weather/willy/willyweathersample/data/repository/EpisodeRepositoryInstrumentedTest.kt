package com.weather.willy.willyweathersample.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.weather.willy.willyweathersample.data.local.Database
import com.weather.willy.willyweathersample.data.local.EpisodeDao
import com.weather.willy.willyweathersample.data.network.EpisodeApiIml
import com.weather.willy.willyweathersample.data.network.NetworkAPI
import com.weather.willy.willyweathersample.data.dummyEpisodeResponse
import com.weather.willy.willyweathersample.model.EpisodeLoadingError
import com.weather.willy.willyweathersample.util.getValueForTest
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class EpisodeRepositoryInstrumentedTest : AbstractRepositoryTest() {


    private lateinit var mEpisodeRepository: EpisodeRepository
    private lateinit var mEpisodeDao: EpisodeDao

    override fun setup(database: Database, networkAPI: NetworkAPI) {
        mEpisodeDao = database.episodeDao()
        mEpisodeRepository = EpisodeRepositoryImpl(mEpisodeDao, EpisodeApiIml(networkAPI))
    }

    @Test
    fun fetchAndInsertException() {
        exceptionRule.expect(EpisodeLoadingError::class.java)
        setGenerateError(true)
        runBlocking {
            mEpisodeRepository.fetchAndSaveEpisode(1)
        }
    }

    @Test
    fun fetchAndInsert() {
        val episodeId = 1
        val dummyEpisode = dummyEpisodeResponse(episodeId)
        setGenerateError(false)
        setResult(dummyEpisode)
        runBlocking {
            mEpisodeRepository.fetchAndSaveEpisode(episodeId)
        }
        assertEquals(mEpisodeRepository.episode(episodeId).getValueForTest().id, episodeId)
    }
}