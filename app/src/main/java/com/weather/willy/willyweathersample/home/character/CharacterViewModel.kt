package com.weather.willy.willyweathersample.home.character

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.weather.willy.willyweathersample.OpenForTesting
import com.weather.willy.willyweathersample.R
import com.weather.willy.willyweathersample.data.ServiceLocator
import com.weather.willy.willyweathersample.data.repository.CharacterRepository
import com.weather.willy.willyweathersample.model.CharacterListLoadingError
import com.weather.willy.willyweathersample.model.local.Character
import com.weather.willy.willyweathersample.model.local.CharacterWithEpisode
import com.weather.willy.willyweathersample.util.InternetConnectivityManager
import kotlinx.coroutines.*

@OpenForTesting
class CharacterViewModel(
    private val characterRepository: CharacterRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {

    private var mJob: Job? = null

    private val _showProgress = MutableLiveData(false)

    private val _selectedCharacterLiveData: MutableLiveData<Int> = MutableLiveData()

    private var _selectedCharacterName: String? = null

    private val _navigateOnCharacterSelected = MutableLiveData<Boolean>()

    private val _snackbarMessageRes = MutableLiveData(-1)

    fun navigateOnCharacterSelected(): LiveData<Boolean> = _navigateOnCharacterSelected

    fun snackbarMessageRes(): LiveData<Int> = _snackbarMessageRes

    fun resetSnackbarMessageRes() {
        _snackbarMessageRes.postValue(-1)
    }

    fun selectedCharacterName(): String? = _selectedCharacterName

    fun showProgress(): LiveData<Boolean> = _showProgress

    fun resetNavigateOnCharacterSelected() {
        _navigateOnCharacterSelected.postValue(false)
    }

    fun selectedCharacterLiveData(): LiveData<CharacterWithEpisode> =
        _selectedCharacterLiveData.switchMap {
            characterRepository.fetchCharacterById(it)
        }

    private val mExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when (throwable) {
            is CharacterListLoadingError -> {
                showProgress(false)
            }

            else -> {
                throwable.printStackTrace()
            }
        }
    }

    val pagedCharacterListLiveData: LiveData<PagedList<Character>> by lazy {
        LivePagedListBuilder(
            characterRepository.paginatedCharacterListDataSource(),
            PagedList.Config.Builder().setPageSize(20).setEnablePlaceholders(false).build()
        )
            .setBoundaryCallback(object : PagedList.BoundaryCallback<Character>() {

                override fun onItemAtEndLoaded(itemAtEnd: Character) {
                    fetchCharacters()
                }

                override fun onZeroItemsLoaded() {
                    fetchCharacters()
                }

            }).build()
    }

    fun fetchCharacters() {
        if (InternetConnectivityManager.isInternetConnected()) {
            mJob?.cancel()
            mJob = viewModelScope.launch(defaultDispatcher + mExceptionHandler)
            {
                showProgress(true)
                characterRepository.fetchCharacterList()
                showProgress(false)
            }
        } else {
            _snackbarMessageRes.postValue(R.string.no_internet_connection)
        }
    }

    fun onCharacterSelected(character: Character) {
        if (_selectedCharacterLiveData.value != character.characterId) {
            _selectedCharacterName = character.name
            _selectedCharacterLiveData.postValue(character.characterId)
            _navigateOnCharacterSelected.postValue(true)
        }
    }

    private fun showProgress(value: Boolean) {
        viewModelScope.launch(mainDispatcher) {
            _showProgress.postValue(value)
        }
    }

}

class CharacterViewModelFactory(private val mCharacterRepository: CharacterRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CharacterViewModel::class.java))
            CharacterViewModel(mCharacterRepository, Dispatchers.IO, Dispatchers.Main) as T
        else super.create(modelClass)
    }
}

fun Fragment.provideCharacterViewModelFactory(): ViewModelProvider.Factory = ServiceLocator.mFactory
    ?: CharacterViewModelFactory(ServiceLocator.provideCharacterRepository())

fun Activity.provideCharacterViewModelFactory(): ViewModelProvider.Factory = ServiceLocator.mFactory
    ?: CharacterViewModelFactory(ServiceLocator.provideCharacterRepository())