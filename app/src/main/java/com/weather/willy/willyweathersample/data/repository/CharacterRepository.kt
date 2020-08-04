package com.weather.willy.willyweathersample.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.weather.willy.willyweathersample.data.local.CharacterDao
import com.weather.willy.willyweathersample.data.network.CharacterApi
import com.weather.willy.willyweathersample.data.wrapEspressoIdlingResource
import com.weather.willy.willyweathersample.model.CharacterListLoadingError
import com.weather.willy.willyweathersample.model.local.Character
import com.weather.willy.willyweathersample.model.network.NetworkResponse

interface CharacterRepository {

    suspend fun fetchCharacterList()

    fun paginatedCharacterListDataSource(): DataSource.Factory<Int, Character>

    fun characterList(): LiveData<List<Character>>

    fun fetchCharacterById(characterId: Int): LiveData<Character>
}


class CharacterRepositoryImpl constructor(
    private val characterApi: CharacterApi,
    private val characterDao: CharacterDao
) : CharacterRepository {

    private var mLimitNotReached = true

    override suspend fun fetchCharacterList() {
        if (mLimitNotReached) {
            wrapEspressoIdlingResource {

                val count = characterDao.fetchCharacterCount()
                val response = characterApi.fetchCharacterList(count / 20 + 1)
                if (response is NetworkResponse.Success) {
                    val list = mutableListOf<Character>()
                    response.data.first.mapTo(list) {
                        Character(it)
                    }
                    characterDao.saveCharacterList(list)
                    mLimitNotReached = !response.data.second
                } else {
                    throw CharacterListLoadingError()
                }
            }
        }
    }

    override fun characterList(): LiveData<List<Character>> =
        characterDao.fetchCharacterList()

    override fun paginatedCharacterListDataSource(): DataSource.Factory<Int, Character> =
        characterDao.fetchPagedCharacterList()

    override fun fetchCharacterById(characterId: Int): LiveData<Character> =
        characterDao.fetchCharacter(characterId)

}