package com.luna.movieapp.ui.single_movie_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.luna.movieapp.data.repository.NetworkState
import com.luna.movieapp.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class SingleMovieViewModel (private val movieRepository: MovieDetailsRepository, movieId: Int) : ViewModel(){

    private val compositeDisposable  = CompositeDisposable()

    val movieDetails : LiveData<MovieDetails> by lazy { //by lazy: view model이 초기화되었을 때가 아니라 우리가 필요할 때 데이터를 가져온다
        movieRepository.fetchSingleMovieDetails(compositeDisposable, movieId)
    }

    val networkState : LiveData<NetworkState> by lazy {
        movieRepository.getMovieDetailNetworkState()
    }

    override fun onCleared() { //액티비티 혹은 프래그먼트가 destroy될 때
        super.onCleared()
        compositeDisposable.clear()
    }
}