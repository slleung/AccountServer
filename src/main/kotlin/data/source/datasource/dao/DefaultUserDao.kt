package data.source.datasource.dao

import Configs
import com.datastax.driver.core.Cluster
import com.datastax.driver.core.DataType
import com.datastax.driver.core.schemabuilder.SchemaBuilder.*
import com.datastax.driver.mapping.Mapper.Option.*
import com.datastax.driver.mapping.MappingManager
import data.*
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

    private val userMapper by lazy {
        mappingManager.mapper(User::class.java).apply {
            setDefaultSaveOptions(saveNullFields(false))
        }
    }

    private val userByEmailMapper by lazy {
        mappingManager.mapper(UserByEmail::class.java).apply {
            setDefaultSaveOptions(saveNullFields(false))
        }
    }

    init {
        val datacenters = Configs.scyllaDatacenters.map { entry ->
            entry.key to entry.value.toString()
        }.toMap()
        session.execute(
            createKeyspace(USER_KEYSPACE).ifNotExists().with()
                .replication(mapOf("class" to "NetworkTopologyStrategy").plus(datacenters))
        )
        session.execute(
            createTable(USER_KEYSPACE, USER_TABLE).ifNotExists()
                .addPartitionKey(COLUMN_ID, DataType.uuid())
                .addColumn(COLUMN_EMAIL, DataType.text())
                .addColumn(COLUMN_PASSWORD_HASH, DataType.text())
                .addColumn(COLUMN_CREATION_DATE, DataType.timestamp())
                .addColumn(COLUMN_LAST_LOGIN_DATE, DataType.timestamp())
        )
        session.execute(
            "CREATE MATERIALIZED VIEW IF NOT EXISTS $USER_KEYSPACE.$USER_BY_EMAIL_MV AS " +
                    "SELECT * FROM $USER_KEYSPACE.$USER_TABLE " +
                    "WHERE $COLUMN_EMAIL IS NOT NULL " +
                    "PRIMARY KEY($COLUMN_EMAIL, $COLUMN_ID)"
        )

        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() {
                cluster.close()
            }
        })
    }

    /**
     * Creates a user. The caller must check whether the email is unique first.
     *
     * @param user The new user.
     */
    override suspend fun insertUser(user: User) {
        userMapper.save(user, ifNotExists(true))
    }

    override suspend fun getUser(id: UUID): User? {
        return userMapper.get(id)
    }

    override suspend fun getUser(email: String): User? {
        return userByEmailMapper.get(email)?.toUser()
    }

}
