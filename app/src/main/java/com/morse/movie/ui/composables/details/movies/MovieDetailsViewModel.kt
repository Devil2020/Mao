package com.morse.movie.ui.composables.details.movies

import android.os.Bundle
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import com.morse.movie.data.entities.ui.State
import com.morse.movie.data.repository.CacheRepository
import com.morse.movie.data.repository.DetailsRepository
import com.morse.movie.data.repository.ICacheRepository
import com.morse.movie.data.repository.IDetailsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MovieDetailsViewModel(
    private val remoteRepository: IDetailsRepository = DetailsRepository(),
    private val cacheRepository: ICacheRepository = CacheRepository(),
    private val handler: SavedStateHandle
) : ViewModel() {

    companion object Factory {
        class Instance(
            private val repository: IDetailsRepository = DetailsRepository(),
            private val cacheRepository: ICacheRepository = CacheRepository(),
            owner: SavedStateRegistryOwner,
            defaultArgs: Bundle? = null
        ) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
            override fun <T : ViewModel> create(
                key: String,
                modelClass: Class<T>,
                handle: SavedStateHandle
            ): T {
                return MovieDetailsViewModel(repository, cacheRepository, handle) as T
            }
        }

    }

    private val _details = MutableSharedFlow<State>()
    val details: Flow<State> get() = _details

    private val _credits = MutableSharedFlow<State>()
    val credits: Flow<State> get() = _credits

    fun load(id: Int) {
        loadMovieDetails(id)
        loadMovieCredits(id)
    }

    private fun loadMovieDetails(id: Int) {
        remoteRepository.loadMovieDetails(id)
            .onEach { _details.emit(it) }
            .launchIn(viewModelScope)
    }

    private fun loadMovieCredits(id: Int) {
        remoteRepository.loadMovieCredits(id)
            .onEach { _credits.emit(it) }
            .launchIn(viewModelScope)
    }

}