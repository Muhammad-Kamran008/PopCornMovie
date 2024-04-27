package it2161.assignment2.popularmovies.repository

import it2161.assignment2.popularmovies.data.MovieDao
import it2161.assignment2.popularmovies.entity.MovieItem

class MovieRepository(private val movieDao: MovieDao) {

    val allMovies = movieDao.getAll()

    suspend fun insert(movie: MovieItem) {
        movieDao.insertMovie(movie)
    }

    suspend fun delete() {
        movieDao.delete()
    }

}

