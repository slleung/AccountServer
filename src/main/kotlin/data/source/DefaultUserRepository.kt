package data.source

import data.Result
import data.User
import data.source.datasource.UserDataStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * This repository manages users from data sources.
 */
class DefaultUserRepository(
    private val userDataStore: UserDataStore,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserRepository {

    override suspend fun createUser(email: String, password: String): Result<Unit> {
        return withContext(ioDispatcher) {
            userDataStore.createUser(email, password)
        }
    }

}
