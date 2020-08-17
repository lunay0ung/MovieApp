package com.luna.movieapp.ui.single_movie_details

import androidx.lifecycle.LiveData
import com.luna.movieapp.data.api.TheMovieDBInterface
import com.luna.movieapp.data.repository.MovieDetailsNetworkDataSource
import com.luna.movieapp.data.repository.NetworkState
import com.luna.movieapp.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsRepository(private val apiService : TheMovieDBInterface) {

    lateinit var movieDetailsNetworkDataSource: MovieDetailsNetworkDataSource

    fun fetchSingleMovieDetails(compositeDisposable: CompositeDisposable, movieId: Int) :LiveData<MovieDetails> {
        movieDetailsNetworkDataSource = MovieDetailsNetworkDataSource(apiService, compositeDisposable)
        movieDetailsNetworkDataSource.fetchMovieDetails(movieId)

        return movieDetailsNetworkDataSource.downloadMovieDetails
    }

    fun getMovieDetailNetworkState(): LiveData<NetworkState> {
        return movieDetailsNetworkDataSource.networkState
    }
}