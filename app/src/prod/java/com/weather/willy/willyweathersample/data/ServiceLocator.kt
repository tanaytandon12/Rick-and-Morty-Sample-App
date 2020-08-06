package com.weather.willy.willyweathersample.data

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.weather.willy.willyweathersample.BuildConfig
import com.weather.willy.willyweathersample.RickAndMortyApp
import com.weather.willy.willyweathersample.data.local.Database
import com.weather.willy.willyweathersample.data.network.CharacterApi
import com.weather.willy.willyweathersample.data.network.CharacterApiImpl
import com.weather.willy.willyweathersample.data.network.NetworkAPI
import com.weather.willy.willyweathersample.data.repository.CharacterRepository
import com.weather.willy.willyweathersample.data.repository.CharacterRepositoryImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ServiceLocator {

    private var mDatabase: Database? = null
    private var mNetworkAPI: NetworkAPI? = null

    @Volatile
    private var mCharacterRepository: CharacterRepository? = null

    @Volatile
    private var mCharacterApi: CharacterApi? = null

    @androidx.annotation.VisibleForTesting
    var mFactory: ViewModelProvider.Factory? = null


    fun init(context: Context) {
        mDatabase = buildDatabase(context)
        mNetworkAPI = buildNetworkAPI()
    }

    private fun buildDatabase(context: Context): Database {
        val database = Room.databaseBuilder(
            context, Database::class.java, BuildConfig.DATABASE_NAME
        ).build()
        mDatabase = database
        return database
    }

    private fun buildNetworkAPI(): NetworkAPI {
        val networkAPI =
            Retrofit.Builder().baseUrl(BuildConfig.SERVER_URL).client(OkHttpClient.Builder().apply {
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }.build()).addConverterFactory(GsonConverterFactory.create()).build()
                .create(NetworkAPI::class.java)
        mNetworkAPI = networkAPI
        return networkAPI
    }

    private fun getDatabase(): Database = mDatabase ?: buildDatabase(RickAndMortyApp.INSTANCE)

    private fun getNetworkAPI(): NetworkAPI = mNetworkAPI ?: buildNetworkAPI()

    fun provideCharacterRepository(): CharacterRepository {
        synchronized(this) {
            return mCharacterRepository ?: buildCharacterRepository()
        }
    }

    private fun buildCharacterRepository(): CharacterRepository {
        val instance = CharacterRepositoryImpl(
            getCharacterApi(), getDatabase().characterDao(),
            getDatabase().characterWithEpisodeDao()
        )
        mCharacterRepository = instance
        return instance
    }

    private fun getCharacterApi(): CharacterApi {
        synchronized(this) {
            return mCharacterApi ?: buildCharacterApi()
        }
    }

    private fun buildCharacterApi(): CharacterApi {
        val characterApi = CharacterApiImpl(getNetworkAPI())
        mCharacterApi = characterApi
        return characterApi
    }
}