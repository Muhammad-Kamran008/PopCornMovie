package it2161.assignment2.popularmovies.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import it2161.assignment2.popularmovies.R
import it2161.assignment2.popularmovies.entity.MovieItem

class MovieAdapter(
    context: Context,
    private val resource: Int = R.layout.movie_item,
    objects: MutableList<MovieItem>
) : ArrayAdapter<MovieItem>(context, resource, objects) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView = convertView ?: inflater.inflate(resource, parent, false)

        val movie = getItem(position)

        val titleTextView: TextView = rowView.findViewById(R.id.name)
        titleTextView.text = movie?.title

        val posterImageView: ImageView = rowView.findViewById(R.id.image)
        val posterUrl = "https://image.tmdb.org/t/p/original/${movie?.poster_path}"
        loadPosterImage(posterUrl, posterImageView)

        return rowView
    }

    private fun loadPosterImage(url: String, imageView: ImageView) {
        Picasso.get()
            .load(url)
            .resizeDimen(R.dimen.poster_width, R.dimen.poster_height)
            .into(imageView)
    }
}
