package com.weather.willy.willyweathersample.data.repository

import androidx.lifecycle.LiveData
import com.weather.willy.willyweathersample.data.local.EpisodeDao
import com.weather.willy.willyweathersample.data.network.EpisodeApi
import com.weather.willy.willyweathersample.model.EpisodeLoadingError
import com.weather.willy.willyweathersample.model.local.Episode
import com.weather.willy.willyweathersample.model.network.NetworkResponse
import com.weather.willy.willyweathersample.model.network.succeeded
import java.lang.Exception

interface EpisodeRepository {
    fun episode(episodeId: Int): LiveData<Episode>

    suspend fun fetchAndSaveEpisode(episodeId: Int)
}

class EpisodeRepositoryImpl constructor(
    private val episodeDao: EpisodeDao,
    private val episodeApi: EpisodeApi
) : EpisodeRepository {

    override fun episode(episodeId: Int): LiveData<Episode> =
        episodeDao.fetchEpisode(episodeId = episodeId)

    override suspend fun fetchAndSaveEpisode(episodeId: Int) {
        val response = episodeApi.fetchEpisode(episodeId)
        if (response is NetworkResponse.Success) {
            episodeDao.insertEpisode(Episode(response.data))
        } else {
            throw EpisodeLoadingError()
        }
    }
}