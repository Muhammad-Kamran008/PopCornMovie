package it2161.assignment2.popularmovies

import android.content.Context
import it2161.assignment2.popularmovies.entity.MovieItem
import org.json.JSONException
import org.json.JSONObject

class movieDBJsonUtils {

    companion object {
        @Throws(JSONException::class)
        fun getMovieDetailsFromJson(
            context: Context,
            movieDetailsJsonStr: String
        ): List<MovieItem> {
            val parsedMovieData = mutableListOf<MovieItem>()
            val movieDetailsArray = JSONObject(movieDetailsJsonStr).getJSONArray("results")

            for (i in 0 until movieDetailsArray.length()) {
                val movieItem = movieDetailsArray.getJSONObject(i)
                val id = movieItem.getInt("id")
                val posterPath = movieItem.getString("poster_path")
                val adult = movieItem.getBoolean("adult")
                val overview = movieItem.getString("overview")
                val releaseDate = movieItem.getString("release_date")
                val genreIds = movieItem.getString("genre_ids")
                val originalTitle = movieItem.getString("original_title")
                val originalLanguage = movieItem.getString("original_language")
                val title = movieItem.getString("title")
                val backdropPath = movieItem.getString("backdrop_path")
                val popularity = movieItem.getDouble("popularity")
                val voteCount = movieItem.getInt("vote_count")
                val video = movieItem.getBoolean("video")
                val voteAverage = movieItem.getDouble("vote_average")

                val movie = MovieItem(
                    id,
                    posterPath,
                    adult,
                    overview,
                    releaseDate,
                    genreIds,
                    originalTitle,
                    originalLanguage,
                    title,
                    backdropPath,
                    popularity,
                    voteCount,
                    video,
                    voteAverage
                )
                parsedMovieData.add(movie)
            }
            return parsedMovieData
        }
    }
}
