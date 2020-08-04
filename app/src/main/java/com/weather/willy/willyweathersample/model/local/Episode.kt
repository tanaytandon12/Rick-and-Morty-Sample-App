package com.weather.willy.willyweathersample.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.weather.willy.willyweathersample.model.network.EpisodeResponse

@Entity
data class Episode(
    @PrimaryKey val id: Int, val airDate: String,
    val episode: String,
    val createdAt: String
) {

    constructor(response: EpisodeResponse) : this(
        response.id,
        response.airDate,
        response.episode,
        response.createdAt
    )
}