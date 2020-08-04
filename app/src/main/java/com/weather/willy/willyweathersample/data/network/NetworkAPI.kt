package com.weather.willy.willyweathersample.data.network

import com.weather.willy.willyweathersample.model.network.CharacterResponseWrapper
import com.weather.willy.willyweathersample.model.network.EpisodeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NetworkAPI {

    @GET("character/")
    suspend fun characters(@Query("page") pageCount: Int): CharacterResponseWrapper

    @GET("episode/{episode_id}")
    suspend fun episode(@Path("episode_id") episodeId: Int): EpisodeResponse
}