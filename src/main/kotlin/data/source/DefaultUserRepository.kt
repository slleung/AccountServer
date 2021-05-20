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

    override suspend fun insertUser(user: User): Result<Unit> {
        return withContext(ioDispatcher) {
            userDataStore.insertUser(user)
        }
    }

}
