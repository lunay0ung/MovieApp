package com.luna.movieapp.data.repository

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}

class NetworkState (val status: Status, val msg : String) {

    companion object {
        val LOADED : NetworkState //= NetworkState(Status.SUCCESS, "Success")
        val LOADING : NetworkState
        val ERROR : NetworkState

        init {
            LOADED = NetworkState(Status.SUCCESS, "Success")
            LOADING = NetworkState(Status.RUNNING, "Running")
            ERROR = NetworkState(Status.FAILED, "Something went wrong")
        }
    }

}