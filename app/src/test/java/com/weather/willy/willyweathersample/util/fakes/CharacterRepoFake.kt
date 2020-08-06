package com.weather.willy.willyweathersample.util.fakes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.weather.willy.willyweathersample.data.dummyCharacter
import com.weather.willy.willyweathersample.data.repository.CharacterRepository
import com.weather.willy.willyweathersample.model.CharacterListLoadingError
import com.weather.willy.willyweathersample.model.local.Character
import com.weather.willy.willyweathersample.model.local.CharacterWithEpisode
import com.weather.willy.willyweathersample.util.createMockDataSourceFactory

class CharacterRepoFake() : CharacterRepository {

    val list = mutableListOf<Character>()

    init {
        for (index in 1..100) {
            list.add(dummyCharacter(index))
        }
    }

    var throwError = false

    override suspend fun fetchCharacterList() {
        if (throwError)
            throw CharacterListLoadingError()
    }

    override fun paginatedCharacterListDataSource(): DataSource.Factory<Int, Character> =
        createMockDataSourceFactory(list)

    override fun fetchCharacterById(characterId: Int): LiveData<CharacterWithEpisode> =
        MutableLiveData(
            CharacterWithEpisode(
                character = dummyCharacter(characterId),
                episodes = listOf()
            )
        )

    override fun characterList(): LiveData<List<Character>> = MutableLiveData(list)

}