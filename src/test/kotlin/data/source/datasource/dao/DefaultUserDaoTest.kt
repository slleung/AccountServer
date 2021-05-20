package data.source.datasource.dao

import Configs
import com.datastax.driver.core.Cluster
import com.datastax.driver.mapping.Mapper
import com.datastax.driver.mapping.MappingManager
import data.User
import di.daoModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.junit5.KoinTestExtension
import java.util.*
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class DefaultUserDaoTest : KoinTest {

    @JvmField
    @RegisterExtension
    val koinTestExtension = KoinTestExtension.create {
        modules(daoModule)
    }

    // init block of dao is not called before DI
    private val defaultUserDao: UserDao by inject()

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

    @Test
    fun `Create a user`() = runBlockingTest {
        val newUser = User(
            UUID.fromString("123e4567-e89b-42d3-a456-556642440000"),
            "test@email.com",
            "passwordHash",
            Date(1621490432)
        )
        deleteUserFromDatabase(newUser)

        defaultUserDao.insertUser(newUser)

        assertTrue(userExistsInDatabase(newUser), "User not found in database after insert.")
    }

    @Test
    fun `Create a user already exists`() = runBlockingTest {
        val newUser = User(
            UUID.fromString("123e4567-e89b-42d3-a456-556642440000"),
            "test@email.com",
            "passwordHash",
            Date(1621490432)
        )
        insertUserIntoDatabase(newUser)

        defaultUserDao.insertUser(newUser)

        assertTrue(userExistsInDatabase(newUser), "User not found in database after insert.")
    }

    @Test
    fun `Get a user by email`() = runBlockingTest {
        val newUser = User(
            UUID.fromString("123e4567-e89b-42d3-a456-556642440000"),
            "test@email.com",
            "passwordHash",
            Date(1621490432)
        )
        insertUserIntoDatabase(newUser)

        val user = defaultUserDao.getUser(newUser.email)

        assertNotNull(user, "Could not find a user by email.")
    }

    private fun userExistsInDatabase(user: User): Boolean {
        return userMapper.get(user.id) != null
    }

    private fun insertUserIntoDatabase(user: User) {
        userMapper.save(user)
    }

    private fun deleteUserFromDatabase(user: User) {
        userMapper.delete(user)
    }

    private fun deleteUserFromDatabase(id: UUID) {
        userMapper.delete(id)
    }

}
