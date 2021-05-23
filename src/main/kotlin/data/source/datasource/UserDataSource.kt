package data.source.datasource

import data.Result
import data.User
import java.util.*

/**
 * The datastore wraps data from DAO with [Result] and present them to the repositories.
 */
interface UserDataSource {

    suspend fun insertUser(user: User): Result<Unit>

    suspend fun getUser(id: UUID): Result<User>

    suspend fun getUser(email: String): Result<User>

    suspend fun updateUser(user: User): Result<Unit>

}
