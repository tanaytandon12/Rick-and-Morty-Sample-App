package com.weather.willy.willyweathersample.model.network

data class CharacterResponse(
    val id: Int, val name: String, val status: String, val type: String,
    val gender: String, val image: String, val url: String, val created: String,
    val episode: List<String>, val origin: OriginResponse, val location: OriginResponse
)

data class OriginResponse(val name: String, val url: String)

data class CharacterResponseWrapper(val results: List<CharacterResponse>)