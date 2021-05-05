package data.source.datasource

import data.Result
import data.User
import data.source.datasource.dao.UserDao

class DefaultUserDataStore(private val userDao: UserDao): UserDataStore {

    override suspend fun createUser(email: String, password: String): Result<User> {
        TODO("Not yet implemented")
    }

}
