package com.weather.willy.willyweathersample.model.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.weather.willy.willyweathersample.model.network.CharacterResponse

@Entity(indices = arrayOf(Index(value = ["characterId"])))
data class Character(
    @PrimaryKey val characterId: Int,
    val name: String? = null,
    val status: String? = null,
    val type: String? = null,
    val gender: String? = null,
    val image: String? = null,
    val url: String,
    val created: String,
    val origin: String?,
    val location: String?,
    val episodeCount: Int = 0
) {
    constructor(response: CharacterResponse) : this(
        response.id,
        response.name,
        response.status,
        response.type,
        response.gender,
        response.image,
        response.url,
        response.created,
        response.origin.name,
        response.location.name,
        response.episode.size
    )

}

