package com.luna.movieapp.data.api

import com.luna.movieapp.data.vo.MovieDetails
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface TheMovieDBInterface {


    //api key: 78999dfcc0ed1877a71f77fc104a33b2
    // https://api.themoviedb.org/3/movie/popular?api_key=78999dfcc0ed1877a71f77fc104a33b2&page=1
    // https://api.themoviedb.org/3/movie/299534?api_key=78999dfcc0ed1877a71f77fc104a33b2
    // https://api.themoviedb.org/3/

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int): Single<MovieDetails>



}