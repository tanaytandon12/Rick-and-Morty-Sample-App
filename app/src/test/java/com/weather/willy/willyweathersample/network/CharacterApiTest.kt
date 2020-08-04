package com.weather.willy.willyweathersample.network

import com.weather.willy.willyweathersample.data.network.CharacterApi
import com.weather.willy.willyweathersample.data.network.CharacterApiImpl
import com.weather.willy.willyweathersample.data.network.NetworkAPI
import com.weather.willy.willyweathersample.model.network.CharacterResponse
import com.weather.willy.willyweathersample.model.network.CharacterResponseWrapper
import com.weather.willy.willyweathersample.model.network.NetworkResponse
import com.weather.willy.willyweathersample.data.dummyCharacterResponse
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Assert.*

class CharacterApiTest : AbstractNetworkAPITest() {

    private lateinit var mCharacterApi: CharacterApi

    override fun setup(networkAPI: NetworkAPI) {
        mCharacterApi = CharacterApiImpl(networkAPI)
    }

    @Test
    fun characterListApi() {
        setGenerateError(true)
        var networkResponse: NetworkResponse<Pair<List<CharacterResponse>, Boolean>>? = null
        runBlocking {
            networkResponse = mCharacterApi.fetchCharacterList(1)
        }
        assertNotNull(networkResponse)
        assertTrue(networkResponse is NetworkResponse.Failure)
        setGenerateError(false)
        val characters = mutableListOf<CharacterResponse>(
            dummyCharacterResponse(1, 1, 11),
            dummyCharacterResponse(2, 2, 12)
        )
        val response = CharacterResponseWrapper(characters)
        setResult(response)
        runBlocking {
            networkResponse = mCharacterApi.fetchCharacterList(1)
        }
        assertNotNull(networkResponse)
        assertTrue(networkResponse is NetworkResponse.Success)
        assertEquals((networkResponse as NetworkResponse.Success).data.first.size, characters.size)
        assertTrue((networkResponse as NetworkResponse.Success).data.second)
    }
}