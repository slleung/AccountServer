package data.source.datasource.dao

import com.datastax.driver.core.ResultSet
import data.User

/**
 * The Dao performs the actual communication with the database.
 */
interface UserDao {

    suspend fun createUser(email: String, password: String): ResultSet

    suspend fun getUser(email: String): ResultSet

}
