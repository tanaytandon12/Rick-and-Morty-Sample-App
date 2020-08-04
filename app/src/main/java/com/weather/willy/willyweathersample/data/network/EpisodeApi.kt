package com.weather.willy.willyweathersample.data.network

import com.weather.willy.willyweathersample.model.network.EpisodeResponse
import com.weather.willy.willyweathersample.model.network.ErrorResponse
import com.weather.willy.willyweathersample.model.network.NetworkResponse
import com.weather.willy.willyweathersample.util.requestSuccess
import java.lang.Exception

interface EpisodeApi {

    suspend fun fetchEpisode(episodeId: Int): NetworkResponse<EpisodeResponse>
}

class EpisodeApiIml(private val networkAPI: NetworkAPI) : EpisodeApi {

    override suspend fun fetchEpisode(episodeId: Int): NetworkResponse<EpisodeResponse> {
        return try {
            NetworkResponse.Success(networkAPI.episode(episodeId))
        } catch (ex: Exception) {
            NetworkResponse.Failure(ErrorResponse(ex))
        }
    }
}