package com.weather.willy.willyweathersample.model.local

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class CharacterWithEpisode(
    @Embedded
    val character: Character,
    @Relation(
        parentColumn = "characterId",
        entityColumn = "episodeId",
        associateBy = Junction(CharacterEpisodeRelation::class)
    )
    val episodes: List<Episode>
)