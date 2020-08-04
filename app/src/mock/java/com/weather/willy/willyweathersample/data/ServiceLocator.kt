package com.weather.willy.willyweathersample.data

import android.content.Context
import com.weather.willy.willyweathersample.RickAndMortyApp
import com.weather.willy.willyweathersample.data.local.Database
import com.weather.willy.willyweathersample.data.network.CharacterApi
import com.weather.willy.willyweathersample.data.network.CharacterApiImpl
import com.weather.willy.willyweathersample.data.network.NetworkAPI
import com.weather.willy.willyweathersample.data.repository.CharacterRepository
import com.weather.willy.willyweathersample.data.repository.CharacterRepositoryImpl

object ServiceLocator {
    private var mDatabase: Database? = null
    private var mNetworkAPI: NetworkAPI? = null

    @Volatile
    private var mCharacterRepository: CharacterRepository? = null

    @Volatile
    private var mCharacterApi: CharacterApi? = null

    private val mNetworkInterceptor = NetworkInterceptor()

    fun init(context: Context) {
        mDatabase = inMemoryDatabase(context)
        mNetworkAPI = networkAPIForTest(mNetworkInterceptor)
    }

    fun getDatabase(): Database = mDatabase ?: buildDatabase()

    private fun buildDatabase(): Database {
        val database = inMemoryDatabase(RickAndMortyApp.INSTANCE)
        mDatabase = database
        return database
    }

    fun getNetworkAPI(): NetworkAPI = mNetworkAPI ?: buildNetworkAPI()

    private fun buildNetworkAPI(): NetworkAPI {
        val networkAPI = networkAPIForTest(networkInterceptor = mNetworkInterceptor)
        mNetworkAPI = networkAPI
        return networkAPI
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

    fun provideCharacterRepository(): CharacterRepository {
        synchronized(this) {
            return mCharacterRepository ?: buildCharacterRepository()
        }
    }

    private fun buildCharacterRepository(): CharacterRepository {
        val characterRepository =
            CharacterRepositoryImpl(getCharacterApi(), getDatabase().characterDao())
        mCharacterRepository = characterRepository
        return characterRepository
    }


    fun setGenerateRandomError(value: Boolean) {
        mNetworkInterceptor.mGenerateRandomError = value
    }

    fun <T> setData(data: T) {
        mNetworkInterceptor.setData(data)
    }

}