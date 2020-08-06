package com.weather.willy.willyweathersample.model.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.weather.willy.willyweathersample.model.network.EpisodeResponse

@Entity(indices = arrayOf(Index(value = ["episodeId"])))
data class Episode(
    @PrimaryKey val episodeId: Int, val airDate: String? = null,
    val episode: String? = null,
    val createdAt: String? = null,
    val url: String? = null
) {

    constructor(response: EpisodeResponse) : this(
        response.id,
        response.airDate,
        response.episode,
        response.createdAt
    )
}

fun String.getEpisodeIdFromUrl() = this.substring(this.lastIndexOf("/") + 1).toInt()