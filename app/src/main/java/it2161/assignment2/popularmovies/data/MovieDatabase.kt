package it2161.assignment2.popularmovies.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import it2161.assignment2.popularmovies.entity.MovieItem
import kotlinx.coroutines.CoroutineScope

// Define the Room database with entities and version
@Database(entities = [MovieItem::class], version = 1, exportSchema = false)
abstract class MovieRoomDatabase : RoomDatabase() {

    // Define abstract method to access DAO
    abstract fun movieDao(): MovieDao

    companion object {
        // Volatile keyword ensures that the value of INSTANCE is always up-to-date
        @Volatile
        private var INSTANCE: MovieRoomDatabase? = null

        // Function to get the database instance
        fun getDatabase(context: Context, scope: CoroutineScope): MovieRoomDatabase {
            // Only one thread can execute the synchronized block at a time
            return INSTANCE ?: synchronized(this) {
                // Create a new database instance if it doesn't exist
                val instance = buildDatabase(context.applicationContext, scope)
                INSTANCE = instance
                instance
            }
        }

        // Function to build the database
        private fun buildDatabase(appContext: Context, scope: CoroutineScope): MovieRoomDatabase {
            // Build the Room database instance
            return Room.databaseBuilder(
                appContext,
                MovieRoomDatabase::class.java,
                "movie_database"
            )
                // Handle database migration if needed
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
