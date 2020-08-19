package com.luna.movieapp.data.repository

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}

class NetworkState (val status: Status, val msg : String) {

    /*
    **companion object**
    클래스의 인스턴스보다는 클래스에 속한 함수나 프로퍼티가 필요할 경우 companion object 내에서 선언하면 된다.
    companion object는 싱글턴이며, companion object의 멤버들은 멤버를 포함하는 클래스의 이름으로 바로 접근 가능하다.
    (companion object에의 접근을 보다 명확하게 하고싶으면 이름을 붙일 수도 있다.)
    If you need a function or a property to be tied to a class rather than to instances of it (similar to @staticmethod in Python),
    you can declare it inside a companion object
    The companion object is a singleton, and its members can be accessed directly via the name of the containing class
    (although you can also insert the name of the companion object if you want to be explicit about accessing the companion object)

    https://kotlinlang.org/docs/tutorials/kotlin-for-py/objects-and-companion-objects.html
     */
    companion object {
        val LOADED : NetworkState //= NetworkState(Status.SUCCESS, "Success")
        val LOADING : NetworkState
        val ERROR : NetworkState
        val ENDOFLIST: NetworkState

        init {
            LOADED = NetworkState(Status.SUCCESS, "Success")
            LOADING = NetworkState(Status.RUNNING, "Running")
            ERROR = NetworkState(Status.FAILED, "Something went wrong")
            ENDOFLIST = NetworkState(Status.FAILED, "You have reached the end")
        }
    }

}