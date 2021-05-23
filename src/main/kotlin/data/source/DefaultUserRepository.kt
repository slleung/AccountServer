package data.source

import data.Result
import data.User
import data.source.datasource.UserDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

/**
 * This repository manages users from data sources.
 */
class DefaultUserRepository(
    private val userDataSource: UserDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserRepository {

    override suspend fun insertUser(user: User): Result<Unit> {
        return withContext(ioDispatcher) {
            userDataSource.insertUser(user)
        }
    }

    override suspend fun getUser(id: UUID): Result<User> {
        return withContext(ioDispatcher) {
            userDataSource.getUser(id)
        }
    }

    override suspend fun getUser(email: String): Result<User> {
        return withContext(ioDispatcher) {
            userDataSource.getUser(email)
        }
    }
}
