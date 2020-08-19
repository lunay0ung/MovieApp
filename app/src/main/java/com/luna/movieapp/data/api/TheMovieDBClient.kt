package com.luna.movieapp.data.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


const val API_KEY = "78999dfcc0ed1877a71f77fc104a33b2"
const val BASE_URL = "https://api.themoviedb.org/3/"
const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w342"

const val FIRST_PAGE = 1
const val POSTER_PER_PAGE = 20

// https://api.themoviedb.org/3/movie/popular?api_key=6e63c2317fbe963d76c3bdc2b785f6d1&page=1
// https://api.themoviedb.org/3/movie/299534?api_key=6e63c2317fbe963d76c3bdc2b785f6d1
// https://image.tmdb.org/t/p/w342/or06FN3Dka5tukK1e9sl16pB3iy.jpg


/*
object 클래스란

인스턴스를 하나만 가진 싱글턴 클래스가 필요하다면 object 키워드를 붙이면 일반적인 방법으로 클래스를 선언하듯이 목표를 이룰 수 있다.
해당 클래스는 오로지 하나의 인스턴스만 보유할 것이며, 해당 인스턴스의 이름은 클래스의 그것과 같다.
-If you need a singleton - a class that only has got one instance -
you can declare the class in the usual way, but use the object keyword instead of class:
There will only ever be one instance of this class,
and the instance (which is created the first time it is accessed, in a thread-safe manner) has got the same name as the class:

-object declaration은 object 키워드를 통해서만 가능하다. 이렇게 선언된 객체는 프로그램 전체에서 공유할 수 있는 하나뿐인 객체이다.
-어떤 변수, 메소드 등이 static 키워드로 선언되면, 해당 멤버는 자신이 속한 클래스의 어떤 객체가 생성되기 전에도 접근가능하다.


-object로 클래스를 정의하면, 싱클턴(Singleton) 패턴이 적용되어 객체가 한번만 생성되도록 합니다.
자바에서는 싱글턴 패턴을 적용하기 위해 꽤 많은 코드를 작성해야 했는데요, 코틀린에서는 object를 사용하면 이런 형식적인 코드(boilerplate)를 작성하지 않아도 됩니다.
싱글턴으로 사용하는 방법 외에도, object는 익명객체를 생성할 때도 사용됩니다.
https://codechacha.com/ko/kotlin-object-vs-class/
 */
object TheMovieDBClient {

    fun getClinet() : TheMovieDBInterface {

        val requestInterceptor = Interceptor {chain ->

            val url = chain.request()
                .url()
                .newBuilder()
                .addQueryParameter("api_key", API_KEY)
                .build()

            val request = chain.request()
                .newBuilder()
                .url(url)
                .build()

            return@Interceptor chain.proceed(request)
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TheMovieDBInterface::class.java)
    }
}