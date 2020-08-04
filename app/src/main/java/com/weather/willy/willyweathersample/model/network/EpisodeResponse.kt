package com.weather.willy.willyweathersample.model.network

import com.google.gson.annotations.SerializedName

data class EpisodeResponse(
    val id: Int, val name: String,
    @SerializedName("air_data")
    val airDate: String,
    val episode: String,
    @SerializedName("created_at")
    val createdAt: String
)