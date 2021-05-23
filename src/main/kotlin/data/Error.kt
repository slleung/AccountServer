package data

import com.google.rpc.Code

/**
 * Service-wide error objects.
 *
 * The error code corresponds to the code in [com.google.rpc.Code]. It is useful if we need to send the error back.
 */
sealed class Error(val code: Int, val message: String) {

    data class GenericError(val msg: String) : Error(Code.UNKNOWN_VALUE, msg) {
        constructor(e: Exception) : this(e.message ?: "")
    }

    data class AlreadyExistsError(val msg: String) : Error(Code.ALREADY_EXISTS_VALUE, msg)
    data class NotFoundError(val msg: String) : Error(Code.NOT_FOUND_VALUE, msg)

}
