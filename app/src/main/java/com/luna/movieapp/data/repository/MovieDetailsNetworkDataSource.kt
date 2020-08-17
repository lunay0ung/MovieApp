package com.luna.movieapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.luna.movieapp.data.api.TheMovieDBInterface
import com.luna.movieapp.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDetailsNetworkDataSource (private val apiService : TheMovieDBInterface, private val compositeDisposable: CompositeDisposable){

    private val _networkState = MutableLiveData<NetworkState>() //LiveData는 mutable하지 않음
    val networkState:   LiveData<NetworkState>
        get() = _networkState //with this get, no need to implement get function to get networkSate

    private val _downloadMovieDetailsResponse = MutableLiveData<MovieDetails>()
    val downloadMovieDetails: LiveData<MovieDetails>
        get() = _downloadMovieDetailsResponse

    fun fetchMovieDetails(movieId: Int) {
        _networkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(
                apiService.getMovieDetails(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe (
                        {
                            _downloadMovieDetailsResponse.postValue(it)
                            _networkState.postValue(NetworkState.LOADED)
                        }
                    ,
                        {
                            _networkState.postValue(NetworkState.ERROR)
                            Log.e("MovieDetailsDataSource", it.message)
                        }
                    )
            )
        } catch (e: Exception) {
            Log.e("MovieDetailsDataSource", e.message)

        }
    }
}