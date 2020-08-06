package com.weather.willy.willyweathersample.data

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.weather.willy.willyweathersample.data.local.Database
import com.weather.willy.willyweathersample.data.network.NetworkAPI
import com.weather.willy.willyweathersample.model.local.Character
import com.weather.willy.willyweathersample.model.local.Episode
import com.weather.willy.willyweathersample.model.network.CharacterResponse
import com.weather.willy.willyweathersample.model.network.EpisodeResponse
import com.weather.willy.willyweathersample.model.network.OriginResponse
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors

fun Any.networkAPIForTest(networkInterceptor: NetworkInterceptor): NetworkAPI =
    Retrofit.Builder().baseUrl("https://localhost.com")
        .client(OkHttpClient.Builder().apply {
            addInterceptor(networkInterceptor)
        }.build())
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(NetworkAPI::class.java)

fun Any.dummyEpisodeResponse(id: Int) =
    EpisodeResponse(
        id = id, airDate = "air_date$id",
        createdAt = "created_at$id", episode = "episode$id", name = "name$id"
    )

fun Any.dummyCharacterResponse(
    id: Int,
    episodeStartIndex: Int,
    episodeEndIndex: Int
): CharacterResponse {
    val origin =
        OriginResponse(
            "nameOrigin$id",
            "https://localhost.com/$id"
        )
    val location =
        OriginResponse(
            "nameLocation$id",
            "https:://localhost.com/$id"
        )
    val episodes = mutableListOf<String>()
    for (index in episodeStartIndex..episodeEndIndex) {
        episodes.add("https://randomurl.com/$index")
    }
    return CharacterResponse(
        id = id, name = "name$id", status = "status$id", episode = episodes,
        origin = origin, location = location, url = "https://localhost.com",
        image = "https://image.com", created = "crs", gender = "mae", type = "sf"
    )
}

fun Any.inMemoryDatabase(context: Context, setTransactionExecutor: Boolean = false) =
    Room.inMemoryDatabaseBuilder(
        context,
        Database::class.java
    ).apply {
        if (setTransactionExecutor)
            setTransactionExecutor(Executors.newSingleThreadExecutor())
    }
        .build()

fun Any.dummyCharacter(id: Int) =
    Character(
        characterId = id,
        name = "name_$id",
        status = "status_$id",
        type = "type_$id",
        gender = if (id % 2 == 0) "MALE" else "FEMALE",
        created = "created_$id",
        image = "https://google.com",
        location = "location_$id",
        origin = "origin$id", url = "https://"
    )

fun Any.dummyEpisode(id: Int) =
    Episode(
        episodeId = id, episode = "episode_$id", createdAt = "created_$id",
        airDate = "airDate_$id"
    )

class NetworkInterceptor : Interceptor {

    private val gson = Gson()

    private lateinit var mResult: String

    var mGenerateRandomError = false

    fun <T> setData(data: T) {
        mResult = gson.toJson(data)
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        pretendToBlockForNetworkRequest()
        return if (mGenerateRandomError) {
            makeErrorResult(chain.request())
        } else {
            makeSuccessResult(chain.request())
        }
    }


    /**
     * Pretend to "block" interacting with the network.
     *
     * Really: sleep for 5
     * 00ms.
     */
    private fun pretendToBlockForNetworkRequest() = Thread.sleep(500)

    /**
     * Generate an error result.
     *
     * ```
     * HTTP/1.1 500 Bad server day
     * Content-type: application/json
     *
     * {"cause": "not sure"}
     * ```
     */
    private fun makeErrorResult(request: Request): Response {
        val content = gson.toJson(mapOf("cause" to "unkown source"))
        return Response.Builder()
            .code(500)
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .message("Bad server response")
            .body(
                content.toResponseBody("application/json; charset=utf-8".toMediaType())
            )
            .build()
    }

    private fun makeSuccessResult(request: Request): Response {
        return Response.Builder().code(200).request(request)
            .protocol(Protocol.HTTP_1_1)
            .message("OK")
            .body(mResult.toResponseBody("application/json; charset=utf-8".toMediaType())).build()
    }

}