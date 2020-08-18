package com.luna.movieapp.data.vo


import com.google.gson.annotations.SerializedName

data class Movie(
    val id: Int,
    val popularity: Double,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: String,
    val title: String
)