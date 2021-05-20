package data.source.datasource.dao

import data.User
import java.util.*

/**
 * The Dao performs the actual communication with the database.
 */
interface UserDao {

    suspend fun insertUser(user: User)

    suspend fun getUser(id: UUID): User?

    suspend fun getUser(email: String): User?

}
