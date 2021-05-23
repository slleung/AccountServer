package helpers

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.querybuilder.QueryBuilder
import com.datastax.driver.mapping.Mapper
import com.datastax.driver.mapping.MappingManager
import data.USER_KEYSPACE
import data.USER_TABLE
import data.User
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
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
 * Init the [USER_TABLE] table with the supplied users.
 *
 * If none is supplied, the table will be empty.
 */
fun initDb(vararg elements: User) {
    clearUserTable()

    elements.forEach { user ->
        insertUserIntoUserTable(user)
    }
}

private fun clearUserTable() {
    session.execute(QueryBuilder.truncate(USER_KEYSPACE, USER_TABLE))
}

private fun userExistsInUserTable(user: User): Boolean {
    return userMapper.get(user.id) != null
}

private fun insertUserIntoUserTable(user: User) {
    userMapper.save(user)
}

private fun deleteUserFromUserTable(user: User) {
    userMapper.delete(user)
}

private fun deleteUserFromUserTable(id: UUID) {
    userMapper.delete(id)
}

fun existsInDb() = object : Matcher<User> {
    override fun test(value: User) = MatcherResult(
        userExistsInUserTable(value),
        "User $value should exist in user table",
        "User $value should not exist in user table"
    )
}
