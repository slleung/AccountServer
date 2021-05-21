package helpers

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.querybuilder.QueryBuilder
import com.datastax.driver.mapping.Mapper
import com.datastax.driver.mapping.MappingManager
import data.USER_KEYSPACE
import data.USER_TABLE
import data.User
import java.util.*

private val session by lazy {
    val clusterBuilder = Cluster.Builder()
    Configs.scyllaIps.forEach { ip ->
        clusterBuilder.addContactPoint(ip)
    }
    clusterBuilder.build().connect()
}

private val mappingManager by lazy {
    MappingManager(session)
}

private val userMapper by lazy {
    mappingManager.mapper(User::class.java).apply {
        setDefaultSaveOptions(Mapper.Option.saveNullFields(false))
    }
}

/**
 * Init the table with the supplied users.
 *
 * If none is supplied, the table will be empty.
 */
fun initDb(vararg elements: User) {
    clearDatabase()

    elements.forEach { user ->
        insertUserIntoDatabase(user)
    }
}

private fun clearDatabase() {
    session.execute(QueryBuilder.truncate(USER_KEYSPACE, USER_TABLE))
}

fun userExistsInDatabase(user: User): Boolean {
    return userMapper.get(user.id) != null
}

fun insertUserIntoDatabase(user: User) {
    userMapper.save(user)
}

fun deleteUserFromDatabase(user: User) {
    userMapper.delete(user)
}

fun deleteUserFromDatabase(id: UUID) {
    userMapper.delete(id)
}
