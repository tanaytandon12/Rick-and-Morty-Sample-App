package com.weather.willy.willyweathersample.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["episodeId", "characterId"])
data class CharacterEpisodeRelation(
    val episodeId: Int, val characterId: Int
)