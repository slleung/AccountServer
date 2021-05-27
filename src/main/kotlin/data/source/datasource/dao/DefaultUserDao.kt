package data.source.datasource.dao

import Configs
import Constants.COLUMN_CREATION_DATE
import Constants.COLUMN_EMAIL
import Constants.COLUMN_ID
import Constants.COLUMN_LAST_LOGIN_DATE
import Constants.COLUMN_PASSWORD_HASH
import Constants.COLUMN_VERIFICATION_STATE
import Constants.USER_BY_EMAIL_MV
import Constants.ACCOUNT_KEYSPACE
import Constants.USER_TABLE
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
            createKeyspace(ACCOUNT_KEYSPACE).ifNotExists().with()
                .replication(mapOf("class" to "NetworkTopologyStrategy").plus(datacenters))
        )
        session.execute(
            createTable(ACCOUNT_KEYSPACE, USER_TABLE).ifNotExists()
                .addPartitionKey(COLUMN_ID, DataType.uuid())
                .addColumn(COLUMN_EMAIL, DataType.text())
                .addColumn(COLUMN_PASSWORD_HASH, DataType.text())
                .addColumn(COLUMN_CREATION_DATE, DataType.timestamp())
                .addColumn(COLUMN_LAST_LOGIN_DATE, DataType.timestamp())
                .addColumn(COLUMN_VERIFICATION_STATE, DataType.text())
        )
        session.execute(
            "CREATE MATERIALIZED VIEW IF NOT EXISTS $ACCOUNT_KEYSPACE.$USER_BY_EMAIL_MV AS " +
                    "SELECT * FROM $ACCOUNT_KEYSPACE.$USER_TABLE " +
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
    override suspend fun insert(user: User) {
        userMapper.save(user, ifNotExists(true))
    }

    override suspend fun get(id: UUID): User? {
        return userMapper.get(id)
    }

    override suspend fun get(email: String): User? {
        return userByEmailMapper.get(email)?.toUser()
    }

    override suspend fun update(user: User) {
        userMapper.save(user, ifNotExists(false))
    }

}
