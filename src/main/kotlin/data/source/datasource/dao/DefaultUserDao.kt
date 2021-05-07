package data.source.datasource.dao

import com.datastax.driver.core.Cluster
import data.User

class DefaultUserDao: UserDao {

    private val cluster by lazy {
        Cluster.Builder()
            .addContactPoint("")
    }

    override suspend fun createUser(email: String, password: String): User {
        TODO("Not yet implemented")
    }

}
