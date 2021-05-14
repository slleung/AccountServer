package data.source.datasource.dao

import Configs
import com.datastax.driver.core.Cluster
import com.datastax.driver.core.ResultSet
import data.source.datasource.dao.DefaultUserDao.UserStore.UserKeyspace.UserTable.COLUMN_EMAIL
import data.source.datasource.dao.DefaultUserDao.UserStore.UserKeyspace.UserTable.COLUMN_PASSWORD

/**
 * DAO for the user database.
 *
 * The user database stores user credentials and other identifying data.
 */
class DefaultUserDao : UserDao {

    private val cluster by lazy {
        val clusterBuilder = Cluster.Builder()
        Configs.scyllaIps.forEach { ip ->
            clusterBuilder.addContactPoint(ip)
        }
        clusterBuilder.build()
    }

    private val session by lazy {
        cluster.connect()
    }

    init {
        val stringBuilder = StringBuilder()
        val datacenters = Configs.scyllaDatacenters
        datacenters.forEach { entry ->
            stringBuilder.append(", '${entry.key}' : ${entry.value}")
        }
        session.execute("CREATE KEYSPACE IF NOT EXISTS $USER_KEYSPACE WITH replication = {'class': 'NetworkTopologyStrategy'$stringBuilder}")
        session.execute("USE $USER_KEYSPACE")
        session.execute("CREATE TABLE IF NOT EXISTS $USER_TABLE ($COLUMN_EMAIL text, $COLUMN_PASSWORD text, PRIMARY KEY ($COLUMN_EMAIL))")

        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() {
                cluster.close()
            }
        })
    }

    override suspend fun createUser(email: String, password: String): ResultSet {
        return session.execute("INSERT INTO $USER_TABLE ($COLUMN_EMAIL, $COLUMN_PASSWORD) VALUES ($email, $password)")
    }

    private companion object UserStore {
        // helpful aliases
        const val USER_KEYSPACE = UserKeyspace.NAME
        const val USER_TABLE = UserKeyspace.UserTable.NAME

        // data model (schema)
        object UserKeyspace {
            const val NAME = "user_keyspace"

            interface BaseColumns {

            }

            object UserTable : BaseColumns {
                const val NAME = "users"

                const val COLUMN_EMAIL = "email"
                const val COLUMN_PASSWORD = "password"
            }
        }

    }

}
