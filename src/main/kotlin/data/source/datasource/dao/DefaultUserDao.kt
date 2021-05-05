package data.source.datasource.dao

import data.User

class DefaultUserDao: UserDao {

    override suspend fun createUser(email: String, password: String): User {
        TODO("Not yet implemented")
    }

}
