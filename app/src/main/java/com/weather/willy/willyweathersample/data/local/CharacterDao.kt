package com.weather.willy.willyweathersample.data.local

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.weather.willy.willyweathersample.model.local.Character

@Dao
interface CharacterDao {

    @Query("select * from Character")
    fun fetchCharacterList(): LiveData<List<Character>>

    @Query("select * from Character order by characterId")
    fun fetchPagedCharacterList(): DataSource.Factory<Int, Character>

    @VisibleForTesting
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCharacterList(characters: List<Character>)

    @Query("select * from Character where characterId = :characterId")
    fun fetchCharacter(characterId: Int): LiveData<Character>

    @Query("delete  from Character")
    fun deleteAllCharacters()

    @Query("select count(*) from Character")
    fun fetchCharacterCount(): Int
}