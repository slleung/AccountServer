package data.source.datasource.dao

import data.User
import java.util.*

/**
 * The Dao performs the actual communication with the database.
 */
interface UserDao {

    suspend fun insert(user: User)

    suspend fun get(id: UUID): User?

    suspend fun get(email: String): User?

    suspend fun update(user: User)

}
