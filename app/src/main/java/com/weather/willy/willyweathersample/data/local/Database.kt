package com.weather.willy.willyweathersample.data.local

import androidx.room.RoomDatabase
import androidx.room.Database
import com.weather.willy.willyweathersample.BuildConfig
import com.weather.willy.willyweathersample.model.local.Character
import com.weather.willy.willyweathersample.model.local.CharacterEpisodeRelation
import com.weather.willy.willyweathersample.model.local.Episode

@Database(
    version = 1,
    entities = [Episode::class, Character::class, CharacterEpisodeRelation::class],
    exportSchema = false
)
abstract class Database() : RoomDatabase() {

    abstract fun characterDao(): CharacterDao

    abstract fun episodeDao(): EpisodeDao

    abstract fun characterWithEpisodeDao(): CharacterWithEpisodeDao
}