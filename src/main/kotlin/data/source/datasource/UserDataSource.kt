package data.source.datasource

import data.Result
import data.User
import java.util.*

/**
 * The datastore wraps data from DAO with [Result] and present them to the repositories.
 */
interface UserDataSource {

    suspend fun insert(user: User): Result<Unit>

    suspend fun get(id: UUID): Result<User>

    suspend fun get(email: String): Result<User>

    suspend fun update(user: User): Result<Unit>

}
