package com.weather.willy.willyweathersample.data.network

import com.weather.willy.willyweathersample.model.network.CharacterResponse
import com.weather.willy.willyweathersample.model.network.ErrorResponse
import com.weather.willy.willyweathersample.model.network.NetworkResponse
import com.weather.willy.willyweathersample.model.network.isLast

interface CharacterApi {

    suspend fun fetchCharacterList(pageCount: Int): NetworkResponse<Pair<List<CharacterResponse>, Boolean>>

}

class CharacterApiImpl(private val networkAPI: NetworkAPI) : CharacterApi {

    override suspend fun fetchCharacterList(pageCount: Int): NetworkResponse<Pair<List<CharacterResponse>, Boolean>> {
        return try {
            val response = networkAPI.characters(pageCount)
            NetworkResponse.Success(Pair(response.results, response.info?.isLast() ?: true))
        } catch (ex: Exception) {
            NetworkResponse.Failure(ErrorResponse(ex))
        }
    }
}