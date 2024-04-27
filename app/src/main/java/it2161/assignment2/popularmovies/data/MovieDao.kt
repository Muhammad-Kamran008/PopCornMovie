package it2161.assignment2.popularmovies.data
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import it2161.assignment2.popularmovies.entity.MovieItem
import kotlinx.coroutines.flow.Flow
@Dao
interface MovieDao {
    @Insert
    suspend fun insertMovie(movie: MovieItem)

    @Query("SELECT * FROM MovieItem")
    fun getAll(): Flow<List<MovieItem>>

    @Query("DELETE FROM MovieItem")
    suspend fun delete()
}


