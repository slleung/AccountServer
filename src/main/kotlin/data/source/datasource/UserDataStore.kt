package data.source.datasource

import data.Result
import data.User

/**
 * The datastore wraps data from DAO with [Result] and present them to the repositories.
 */
interface UserDataStore {

    suspend fun createUser(email: String, password: String): Result<Unit>

    suspend fun getUser(email: String): Result<User>

}
