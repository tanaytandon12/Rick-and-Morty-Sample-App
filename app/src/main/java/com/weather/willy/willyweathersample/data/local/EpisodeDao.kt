package com.weather.willy.willyweathersample.data.local

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.weather.willy.willyweathersample.model.local.Episode

@Dao
interface EpisodeDao {

    @Query("select * from Episode where episodeId = :episodeId")
    fun fetchEpisode(episodeId: Int): LiveData<Episode>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisode(episode: Episode)

    @VisibleForTesting
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisodes(episodes: List<Episode>)

    @VisibleForTesting
    @Query("select * from Episode")
    fun fetchEpisodes(): List<Episode>
}