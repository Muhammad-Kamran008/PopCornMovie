package it2161.assignment2.popularmovies.ui.viewmodel

import androidx.lifecycle.*
import it2161.assignment2.popularmovies.entity.MovieItem
import it2161.assignment2.popularmovies.repository.MovieRepository
import kotlinx.coroutines.launch

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {

    val allMovies: LiveData<List<MovieItem>> = repository.allMovies.asLiveData()

    fun insert(movie: MovieItem) {
        viewModelScope.launch {
            repository.insert(movie)
        }
    }

    fun delete() {
        viewModelScope.launch {
            repository.delete()
        }
    }
}

class MovieViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            return MovieViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
