package com.weather.willy.willyweathersample.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CharacterEpisodeRelation(
    val episodeId: Int, val characterId: Int,
    @PrimaryKey val key: String
)