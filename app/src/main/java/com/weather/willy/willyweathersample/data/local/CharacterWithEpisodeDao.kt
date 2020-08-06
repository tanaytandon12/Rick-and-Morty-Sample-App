package com.weather.willy.willyweathersample.data.local

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.room.*
import com.weather.willy.willyweathersample.model.local.Character
import com.weather.willy.willyweathersample.model.local.CharacterEpisodeRelation
import com.weather.willy.willyweathersample.model.local.CharacterWithEpisode
import com.weather.willy.willyweathersample.model.local.Episode

@Dao
abstract class CharacterWithEpisodeDao() {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertCharacterEpisodeRelation(relations: List<CharacterEpisodeRelation>)

    @Transaction
    @Query("select * from Character where characterId = :characterId")
    abstract fun characterWithEpisodes(characterId: Int): LiveData<CharacterWithEpisode>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveCharacterList(characters: List<Character>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertEpisodes(episodes: List<Episode>)

    @Transaction
    suspend open fun save(
        characters: List<Character>,
        episodes: List<Episode>,
        relations: List<CharacterEpisodeRelation>
    ) {
        saveCharacterList(characters)
        insertEpisodes(episodes)
        insertCharacterEpisodeRelation(relations)
    }

    @VisibleForTesting
    @Query("select * from CharacterEpisodeRelation")
    abstract fun fetchAll(): List<CharacterEpisodeRelation>
}