package com.weather.willy.willyweathersample.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.weather.willy.willyweathersample.data.local.CharacterDao
import com.weather.willy.willyweathersample.data.local.CharacterWithEpisodeDao
import com.weather.willy.willyweathersample.data.network.CharacterApi
import com.weather.willy.willyweathersample.data.wrapEspressoIdlingResource
import com.weather.willy.willyweathersample.model.CharacterListLoadingError
import com.weather.willy.willyweathersample.model.local.*
import com.weather.willy.willyweathersample.model.network.NetworkResponse

interface CharacterRepository {

    suspend fun fetchCharacterList()

    fun paginatedCharacterListDataSource(): DataSource.Factory<Int, Character>

    fun characterList(): LiveData<List<Character>>

    fun fetchCharacterById(characterId: Int): LiveData<CharacterWithEpisode>
}


class CharacterRepositoryImpl(
    private val characterApi: CharacterApi,
    private val characterDao: CharacterDao,
    private val characterWithEpisodeDao: CharacterWithEpisodeDao
) : CharacterRepository {

    private var mLimitNotReached = true

    override suspend fun fetchCharacterList() {
        if (mLimitNotReached) {
            wrapEspressoIdlingResource {

                val count = characterDao.fetchCharacterCount()
                val response = characterApi.fetchCharacterList(count / 20 + 1)
                if (response is NetworkResponse.Success) {
                    val list = mutableListOf<Character>()
                    val setOfEpisodes = mutableSetOf<Episode>()
                    val characterEpisodeRelations = mutableListOf<CharacterEpisodeRelation>()
                    response.data.first.mapTo(list) {
                        setOfEpisodes.addAll(it.episode.map { url ->
                            val episodeId = url.getEpisodeIdFromUrl()
                            characterEpisodeRelations.add(
                                CharacterEpisodeRelation(
                                    episodeId,
                                    it.id
                                )
                            )
                            Episode(
                                episodeId = episodeId,
                                url = url
                            )

                        })
                        Character(it)
                    }
                    characterWithEpisodeDao.save(
                        characters = list, episodes = setOfEpisodes.toList(),
                        relations = characterEpisodeRelations
                    )
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

    override fun fetchCharacterById(characterId: Int): LiveData<CharacterWithEpisode> =
        characterWithEpisodeDao.characterWithEpisodes(characterId)

}