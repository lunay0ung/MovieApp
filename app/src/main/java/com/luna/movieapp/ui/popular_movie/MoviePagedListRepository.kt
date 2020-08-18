package com.luna.movieapp.ui.popular_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.luna.movieapp.data.api.POSTER_PER_PAGE
import com.luna.movieapp.data.api.TheMovieDBInterface
import com.luna.movieapp.data.repository.MovieDataSource
import com.luna.movieapp.data.repository.MovieDataSourceFactory
import com.luna.movieapp.data.repository.NetworkState
import com.luna.movieapp.data.vo.Movie
import io.reactivex.disposables.CompositeDisposable

class MoviePagedListRepository(private val apiService: TheMovieDBInterface){

    lateinit var moviePagedList : LiveData<PagedList<Movie>>
    lateinit var moviesDataSourceFactory : MovieDataSourceFactory


    fun fetchLiveMoviePagedList (compositeDisposable: CompositeDisposable) : LiveData<PagedList<Movie>> {
        moviesDataSourceFactory = MovieDataSourceFactory(apiService, compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POSTER_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(moviesDataSourceFactory, config).build()

        return moviePagedList
    }

    //networkstate 정보는 movieDataSource에 있으나 현재 클래스에서는 movieDataSource를 초기화하지 않음.
    //movieDataSource는 MovieDataSourceFactory 내에서 초기화했고 moviesLiveDataSource로 값을 post함
    //따라서 moviesLiveDataSource가 가지고 있는 networkState로 접근해야 함
    fun getNetworkState() : LiveData<NetworkState> {
        return Transformations.switchMap<MovieDataSource, NetworkState> (
            moviesDataSourceFactory.moviesLiveDataSource, MovieDataSource::networkState)
        //해당 코드에서 mutable live data로 된 networkState에 접근 후 live data로 transform함 from moviesLiveDataSource

    }



}