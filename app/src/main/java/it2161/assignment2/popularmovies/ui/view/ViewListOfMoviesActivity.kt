package it2161.assignment2.popularmovies.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.activity.viewModels
import it2161.assignment2.popularmovies.ItemDetailActivity
import it2161.assignment2.popularmovies.MovieApplication
import it2161.assignment2.popularmovies.NetworkUtils
import it2161.assignment2.popularmovies.R
import it2161.assignment2.popularmovies.entity.MovieItem
import it2161.assignment2.popularmovies.movieDBJsonUtils
import it2161.assignment2.popularmovies.ui.adapter.MovieAdapter
import it2161.assignment2.popularmovies.ui.viewmodel.MovieViewModel
import it2161.assignment2.popularmovies.ui.viewmodel.MovieViewModelFactory
import kotlinx.coroutines.*

class ViewListOfMoviesActivity : AppCompatActivity() {

    private val SHOW_BY_TOP_RATED = 1
    private val SHOW_BY_POPULAR = 2

    private var displayType = SHOW_BY_TOP_RATED
    private val movieViewModel: MovieViewModel by viewModels {
        MovieViewModelFactory((application as MovieApplication).repository)
    }
    private var currentPosition = 0

    private var moviesAdapter: MovieAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_list_of_movies)
        observeMovieData()
    }

    private fun observeMovieData() {
        movieViewModel.allMovies.observe(this, Observer { movies ->
            movies?.let {
                moviesAdapter = MovieAdapter(this, R.layout.movie_item, it.toMutableList())
                val listView: ListView = findViewById(R.id.movielist)
                listView.setSelection(currentPosition)
                listView.adapter = moviesAdapter
                registerForContextMenu(listView)
                listView.setOnItemClickListener { adapterView, view, position, _ ->
                    val movie: MovieItem = adapterView.getItemAtPosition(position) as MovieItem
                    goToMovieDetail(movie)
                }
            }
        })
    }

    private fun goToMovieDetail(movie: MovieItem) {
        val intent = Intent(this, ItemDetailActivity::class.java).apply {
            putExtra("overview", movie.overview)
            putExtra("release_date", movie.release_date)
            putExtra("popularity", movie.popularity)
            putExtra("vote_count", movie.vote_count)
            putExtra("vote_average", movie.vote_average)
            putExtra("language", movie.original_language)
            putExtra("adult", movie.adult)
            putExtra("video", movie.video)
            putExtra("poster_path", movie.poster_path)
        }
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        loadMovieData(displayType)
    }

    private fun loadMovieData(viewType: Int) {
        val showTypeStr = when (viewType) {
            SHOW_BY_TOP_RATED -> NetworkUtils.TOP_RATED_PARAM
            SHOW_BY_POPULAR -> NetworkUtils.POPULAR_PARAM
            else -> null
        }

        showTypeStr?.let { type ->
            displayType = viewType
            CoroutineScope(Job() + Dispatchers.IO).launch {
                val movieRequestUrl = NetworkUtils.buildUrl(type, getString(R.string.moviedb_api_key))

                try {
                    val jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl!!)
                    val responseList = movieDBJsonUtils.getMovieDetailsFromJson(this@ViewListOfMoviesActivity, jsonMovieResponse!!)
                    responseList?.let { movies ->
                        withContext(Dispatchers.Main) {
                            movieViewModel.delete()
                            movies.forEach { movie ->
                                movieViewModel.insert(movie)
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item?.itemId) {
            R.id.sortPopular -> {
                loadMovieData(SHOW_BY_POPULAR)
                true
            }
            R.id.sortTopRated -> {
                loadMovieData(SHOW_BY_TOP_RATED)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        val listView: ListView = findViewById(R.id.movielist)
        listView.setSelection(currentPosition)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val listView: ListView = findViewById(R.id.movielist)
        currentPosition = listView.firstVisiblePosition
        outState.putInt("current_position", currentPosition)
    }
}
