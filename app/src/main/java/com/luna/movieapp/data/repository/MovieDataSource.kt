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


    /*
    .subscribeOn(Schedulers.io())
    subscribeOn() 함수는 구독자가 Observable에 subscribe() 함수를 호출하여 구독할 때 실행되는 스레드를 지정하며,
    Schedulers 스케줄러는 RxJava 코드를 어느 스레드에서 실행할지 지정할 수 있다.

    RxJava의 멋진 점은 특정 스케줄러를 사용하다가 다른 스케줄러로 변경하기 쉽다는 것이다.
    RxJava에서 제공하는 스케줄러는 io.reactivex.schedulers 패키지 Schedulers 클래스의 정적 팩토리 메서드로 생성한다.

    RxJava에서 추천하는 스케줄러는 크게 세 가지이다.
        1) 계산(computation) 스케줄러
        2) IO 스케줄러
        3) 트램펄린 스케줄러
        *뉴 스레드 스케줄러는 특수한 상황에서 사용하길 권장

        계산 스케줄러는 CPU에 대응하는 계산용 스케줄러이다. '계산' 작업을 할 때는 대기 시간 없이 빠르게 결과를 도출하는 것이 중요하다.
    계산 작업이라는 말이 어렵게 느껴진다면 입출력 작업을 하지 않는 스케줄러라고 생각하면 된다.
    내부적으로 스레드풀을 생성하며 스레드 개수는 기본적으로 프로세스 개수와 동일하다(변경은 가능하지만 권장사항은 아니다).

        IO 스케줄러는 네트워크 상의 요청을 처리하거나 각종 입출력 장업을 실행하기 위한 스케줄러이다.
    계산 스케줄러와의 다른 점은 기본으로 생성되는 스레드 개수가 다르다는 것이다.
    즉 계산 스케줄러는 CPU 개수만큼 스레드를 생성하지만 IO 스케줄러는 필요할 때마다 스레드를 계속 생성한다.
    입출력 작업은 비동기로 실행되지만 결과를 얻기까지 대기시간이 길다.

        트램펄린 스케줄러는 새로운 스레드를 생성하지 않고 현재 스레드에 무한한 크기의 대기행렬(queue)를 생성하는 스케줄러이다.
    RxJava 1.x에서는 repeat() 함수와 retry() 함수의 기본 스케줄러였으나 2.x에서는 이런 제약이 사라졌다.
    새로운 스레드를 생성하지 않는다는 것과 대기 행렬을 자동으로 만들어준다는 것이 뉴 스레드 스케줄러, 계산 스케줄러, IO 스케줄러와 다르다.
    새로운 스레드를 생성하지 않고 main 스레드에서 모든 작업을 수행하며, 큐에 작업을 넣은 후 1개씩 꺼내어 동작하므로
    첫 번째 구독과 두 번째 구독의 실행 순서가 바뀌는 경우는 발생하지 않는다.

        싱글 스레드 스케줄러는 RxJava 내부에서 단일 스레드를 별도로 생성하여 구독 작업을 처리한다.
    단 생성된 스레드는 여러 번 구독 요청이 와도 공통으로 사용한다.
    리액티브 프로그래밍이 비동기 프로그래밍을 지향하기 때문에 싱글 스레드 스케줄러를 활용할 확률은 낮다.

        자바에서는 java.util.current 패키지에서 제공하는 실행자Executor를 변환하여 스케줄러를 생성할 수 있다.
     하지만 Executor클래스와 Executor변환 스케줄러의 동작 방식이 다르므로 추천 방법은 아니다.
     기존에 사용하던 Executor 클래스를 재사용할 때만 한정적으로 활용한다.
     Executor 변수는 고정 개수(10개)의 스레드풀을 생성한다.

    -
    *요약*
    뉴 스케줄러: 요청을 받을 때마다 새로운 스레드 생성. 적극적으로 추천하는 방법은 아니다.
    계산 스케줄러: 일반적인 계산 작업
    IO 스케줄러: 네트워크 상의 요청, 파일 입출력, DB 쿼리 등

    출처: (도서) RxJava 프로그래밍

     */



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