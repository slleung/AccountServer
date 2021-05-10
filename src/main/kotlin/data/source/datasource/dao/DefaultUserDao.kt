package data.source.datasource.dao

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.ResultSet
import data.User

private const val USER_KEYSPACE = "user_keyspace"
private const val USER_TABLE = "user_table"

private const val COLUMN_ID = "id"
private const val COLUMN_EMAIL = "email"
private const val COLUMN_PASSWORD = "password"

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
        session.execute("CREATE KEYSPACE IF NOT EXISTS $USER_KEYSPACE WITH replication = {'class': 'NetworkTopologyStrategy'$stringBuilder};")
        session.execute("CREATE TABLE IF NOT EXISTS $USER_KEYSPACE.$USER_TABLE ($COLUMN_ID int, $COLUMN_EMAIL text, $COLUMN_PASSWORD text, PRIMARY KEY ($COLUMN_ID));")

        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() {
                cluster.close()
            }
        })
    }

    override suspend fun createUser(email: String, password: String): ResultSet {
        return session.execute("INSERT INTO $USER_KEYSPACE.$USER_TABLE ($COLUMN_EMAIL, $COLUMN_PASSWORD) VALUES ($email, $password);")
    }

}
