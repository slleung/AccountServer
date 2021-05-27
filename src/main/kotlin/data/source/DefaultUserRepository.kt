package data.source

import data.Result
import data.User
import data.source.datasource.EmailVerificationCodeDataSource
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
    private val emailVerificationCodeDataSource: EmailVerificationCodeDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserRepository {

    override suspend fun insertUser(user: User): Result<Unit> {
        return withContext(ioDispatcher) {
            userDataSource.insert(user)
        }
    }

    override suspend fun getUser(id: UUID): Result<User> {
        return withContext(ioDispatcher) {
            userDataSource.get(id)
        }
    }

    override suspend fun getUser(email: String): Result<User> {
        return withContext(ioDispatcher) {
            userDataSource.get(email)
        }
    }

    override suspend fun updateUser(user: User): Result<Unit> {
        return withContext(ioDispatcher) {
            userDataSource.update(user)
        }
    }


}
