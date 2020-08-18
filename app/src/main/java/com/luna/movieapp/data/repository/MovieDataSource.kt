package com.luna.movieapp.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.luna.movieapp.data.api.FIRST_PAGE
import com.luna.movieapp.data.api.TheMovieDBInterface
import com.luna.movieapp.data.vo.Movie
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDataSource(private val apiService : TheMovieDBInterface, private val compositeDisposable: CompositeDisposable)
    : PageKeyedDataSource<Int, Movie>() {

    private var page = FIRST_PAGE
    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    //첫 페이지 로드
    override fun loadInitial(
        params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Movie>
    ) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            apiService.getPopularMovie(page)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        callback.onResult(it.movieList, null, page+1)
                        networkState.postValue(NetworkState.LOADED)
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e(MovieDataSource::class.java.simpleName, it.message)
                    }
                )
        )
    }

    //다음 페이지 로드
    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            apiService.getPopularMovie(params.key) //다음페이지의 키, 자동으로 증가
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        if(it.totalPages >= params.key) { //로드할 페이지가 더 있음 (현재 페이지의 키가 남은 페이지 수보다 작음)
                            callback.onResult(it.movieList, params.key+1) //이전 페이지 키는 필요 없음
                            networkState.postValue(NetworkState.LOADED)
                        }
                        else{
                            networkState.postValue(NetworkState.ENDOFLIST) //페이지의 끝에 도달
                        }
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e(MovieDataSource::class.java.simpleName, it.message)
                    }
                )
        )
    }

    //이전 페이지 로드 -리사이클러뷰가 이전 데이터를 hold하고 있기 때문에 구현x
    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
    }
}