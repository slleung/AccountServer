package data.source

import data.Result
import data.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * This repository manages users from data sources.
 */
class DefaultUserRepository(private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) : UserRepository {

    override suspend fun createUser(email: String, password: String): Result<User> {
        TODO("Not yet implemented")
    }

}
