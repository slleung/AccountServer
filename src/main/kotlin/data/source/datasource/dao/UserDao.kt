package data.source.datasource.dao

import data.User

/**
 * The Dao performs the actual communication with the database.
 */
interface UserDao {

    suspend fun createUser(email: String, password: String): User

}
