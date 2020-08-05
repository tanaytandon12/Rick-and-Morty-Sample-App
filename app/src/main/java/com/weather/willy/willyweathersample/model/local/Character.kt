package com.weather.willy.willyweathersample.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.weather.willy.willyweathersample.model.network.CharacterResponse

@Entity
data class Character(
    @PrimaryKey val id: Int,
    val name: String,
    val status: String,
    val type: String,
    val gender: String,
    val image: String,
    val url: String,
    val created: String,
    val origin: String?,
    val location: String?
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
        response.location.name
    )

}

