package com.weather.willy.willyweathersample.home.character

import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.weather.willy.willyweathersample.OpenForTesting
import com.weather.willy.willyweathersample.data.EspressoIdlingResource
import com.weather.willy.willyweathersample.data.ServiceLocator
import com.weather.willy.willyweathersample.data.repository.CharacterRepository
import com.weather.willy.willyweathersample.data.wrapEspressoIdlingResource
import com.weather.willy.willyweathersample.model.CharacterListLoadingError
import com.weather.willy.willyweathersample.model.local.Character
import kotlinx.coroutines.*

@OpenForTesting
class CharacterViewModel(
    private val characterRepository: CharacterRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {

    private var mJob: Job? = null

    private val _showProgress: MutableLiveData<Boolean> = MutableLiveData(false)

    private val _selectedCharacterLiveData: MutableLiveData<Int> = MutableLiveData()

    fun showProgress(): LiveData<Boolean> = _showProgress

    fun selectedCharacterLiveData(): LiveData<Character> = _selectedCharacterLiveData.switchMap {
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
        mJob?.cancel()
        mJob = viewModelScope.launch(defaultDispatcher + mExceptionHandler)
        {
            showProgress(true)
            characterRepository.fetchCharacterList()
            showProgress(false)
        }
    }

    fun onCharacterSelected(character: Character) {
        if (_selectedCharacterLiveData.value != character.id)
            _selectedCharacterLiveData.postValue(character.id)
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