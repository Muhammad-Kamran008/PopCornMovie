package it2161.assignment2.popularmovies



import android.app.Application
import it2161.assignment2.popularmovies.data.MovieRoomDatabase
import it2161.assignment2.popularmovies.repository.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MovieApplication : Application() {
    // No need to cancel this scope as it'll be torn down with the process
    val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { MovieRoomDatabase.getDatabase(this,applicationScope) }
    val repository by lazy { MovieRepository(database.movieDao()) }
}