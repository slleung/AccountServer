package data.source.datasource.dao

import Configs
import com.datastax.driver.core.Cluster
import com.datastax.driver.core.ResultSet
import com.datastax.driver.mapping.MappingManager
import data.source.datasource.dao.UserDao.UserStore.USER_KEYSPACE
import data.source.datasource.dao.UserDao.UserStore.USER_TABLE
import data.source.datasource.dao.UserDao.UserStore.UserKeyspace.UserTable.COLUMN_CREATION_DATE
import data.source.datasource.dao.UserDao.UserStore.UserKeyspace.UserTable.COLUMN_EMAIL
import data.source.datasource.dao.UserDao.UserStore.UserKeyspace.UserTable.COLUMN_LAST_LOGIN_DATE
import data.source.datasource.dao.UserDao.UserStore.UserKeyspace.UserTable.COLUMN_PASSWORD
import kotlinx.datetime.LocalDate
import java.util.*

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

    private val mappingManager by lazy {
        MappingManager(session)
    }

    init {
        val stringBuilder = StringBuilder()
        val datacenters = Configs.scyllaDatacenters
        datacenters.forEach { entry ->
            stringBuilder.append(", '${entry.key}' : ${entry.value}")
        }
        session.execute("CREATE KEYSPACE IF NOT EXISTS $USER_KEYSPACE WITH replication = {'class': 'NetworkTopologyStrategy'$stringBuilder}")
        session.execute("USE $USER_KEYSPACE")
        session.execute(
            "CREATE TABLE IF NOT EXISTS $USER_TABLE (" +
                    "$COLUMN_EMAIL TEXT, " +
                    "$COLUMN_PASSWORD TEXT, " +
                    "$COLUMN_CREATION_DATE TIMESTAMP, " +
                    "$COLUMN_LAST_LOGIN_DATE TIMESTAMP, " +
                    "PRIMARY KEY ($COLUMN_EMAIL)" +
                    ")"
        )

        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() {
                cluster.close()
            }
        })
    }

    /**
     * Creates a user.
     *
     * @param email The email of the user.
     * @param password The hashed password of the user.
     * @return An empty ResultSet. If user already exists, [ResultSet.wasApplied] will return false, else true.
     */
    override suspend fun createUser(email: String, password: String): ResultSet {
        return session.execute("INSERT INTO $USER_TABLE ($COLUMN_EMAIL, $COLUMN_PASSWORD, $COLUMN_CREATION_DATE) VALUES ('$email', '$password', ${}) IF NOT EXISTS")
    }

    override suspend fun getUser(email: String): ResultSet {
        return session.execute("SELECT * FROM $USER_TABLE WHERE $COLUMN_EMAIL = '$email'")
    }

}
