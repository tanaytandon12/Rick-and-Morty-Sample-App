package com.weather.willy.willyweathersample.data.network

import com.weather.willy.willyweathersample.model.network.CharacterResponse
import com.weather.willy.willyweathersample.model.network.ErrorResponse
import com.weather.willy.willyweathersample.model.network.NetworkResponse

interface CharacterApi {

    suspend fun fetchCharacterList(pageCount: Int): NetworkResponse<List<CharacterResponse>>

}

class CharacterApiImpl(private val networkAPI: NetworkAPI) : CharacterApi {

    override suspend fun fetchCharacterList(pageCount: Int): NetworkResponse<List<CharacterResponse>> {
        return try {
            NetworkResponse.Success(networkAPI.characters(pageCount).results)
        } catch (ex: Exception) {
            NetworkResponse.Failure(ErrorResponse(ex))
        }
    }
}