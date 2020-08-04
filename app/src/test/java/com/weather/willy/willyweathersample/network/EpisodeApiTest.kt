package com.weather.willy.willyweathersample.network

import com.weather.willy.willyweathersample.data.network.EpisodeApi
import com.weather.willy.willyweathersample.data.network.EpisodeApiIml
import com.weather.willy.willyweathersample.data.network.NetworkAPI
import com.weather.willy.willyweathersample.model.network.EpisodeResponse
import com.weather.willy.willyweathersample.model.network.NetworkResponse
import com.weather.willy.willyweathersample.data.dummyEpisodeResponse
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Assert.*

class EpisodeApiTest : AbstractNetworkAPITest() {


    private lateinit var mEpisodeApi: EpisodeApi

    override fun setup(networkAPI: NetworkAPI) {
        mEpisodeApi =
            EpisodeApiIml(
                networkAPI
            )
    }

    @Test
    fun testEpisodeApi() {
        val episodeResponseId = 1
        val dummyEpisodeResponse = dummyEpisodeResponse(episodeResponseId)
        setGenerateError(true)
        var result: NetworkResponse<EpisodeResponse>? = null
        runBlocking {
            result = mEpisodeApi.fetchEpisode(episodeResponseId)
        }
        assertNotNull(result)
        assertTrue(result is NetworkResponse.Failure)
        setGenerateError(false)
        setResult(dummyEpisodeResponse)
        runBlocking {
            result = mEpisodeApi.fetchEpisode(episodeResponseId)
        }
        assertNotNull(result)
        assertTrue(result is NetworkResponse.Success)
        assertEquals(
            (result as NetworkResponse.Success<EpisodeResponse>).data.id,
            episodeResponseId
        )
    }
}