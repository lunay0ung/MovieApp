package com.luna.movieapp.ui.popular_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.luna.movieapp.data.repository.NetworkState
import com.luna.movieapp.data.vo.Movie
import io.reactivex.disposables.CompositeDisposable

class MainActivityViewModel(private val moviePagedListRepository: MoviePagedListRepository)
    : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val moviePagedList : LiveData<PagedList<Movie>> by lazy {
        moviePagedListRepository.fetchLiveMoviePagedList(compositeDisposable)
    }

    val networkState : LiveData<NetworkState> by lazy {
        moviePagedListRepository.getNetworkState()
    }

    fun listIsEmpty() : Boolean {
        return moviePagedList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}