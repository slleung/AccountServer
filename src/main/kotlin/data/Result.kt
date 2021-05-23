package data

sealed class Result<out R> {

    data class Success<out T>(val data: T) : Result<T>()

    data class Failure(val error: Error) : Result<Nothing>()

}
