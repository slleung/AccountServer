package data.source.datasource.dao

import Configs
import com.datastax.driver.core.Cluster
import data.source.datasource.dao.DefaultUserDao.UserStore.USER_KEYSPACE
import data.source.datasource.dao.DefaultUserDao.UserStore.USER_TABLE
import data.source.datasource.dao.DefaultUserDao.UserStore.UserKeyspace.UserTable.COLUMN_EMAIL
import data.source.datasource.dao.DefaultUserDao.UserStore.UserKeyspace.UserTable.COLUMN_PASSWORD
import di.daoModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.*
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DefaultUserDaoTest : KoinTest {

    @BeforeAll
    fun setUpAll() {
        startKoin {
            modules(daoModule)
        }
    }

    private val userDao: UserDao by inject()

    private val session by lazy {
        val clusterBuilder = Cluster.Builder()
        Configs.scyllaIps.forEach { ip ->
            clusterBuilder.addContactPoint(ip)
        }
        clusterBuilder.build().connect()
    }

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Create a user`() = runBlockingTest {
        val email = "test@email.com"
        val password = "password"   // pretend this is hashed
        deleteUserFromDatabase(email)

        val resultSet = userDao.createUser(email, password)

        assertTrue(resultSet.wasApplied())
        assertTrue(userExistsInDatabase(email))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Create a user already exists`() = runBlockingTest {
        val email = "test@email.com"
        val password = "password"   // pretend this is hashed
        insertUserIntoDatabase(email, password)

        val resultSet = userDao.createUser(email, password)

        assertFalse(resultSet.wasApplied())
        assertTrue(userExistsInDatabase(email))
    }

    private fun userExistsInDatabase(email: String): Boolean {
        return session.execute("SELECT count(*) FROM $USER_KEYSPACE.$USER_TABLE WHERE $COLUMN_EMAIL = '$email'")
            .count() == 1
    }

    private fun insertUserIntoDatabase(email: String, password: String) {
        session.execute("INSERT INTO $USER_KEYSPACE.$USER_TABLE ($COLUMN_EMAIL, $COLUMN_PASSWORD) VALUES ('$email', '$password')")
    }

    private fun deleteUserFromDatabase(email: String) {
        session.execute("DELETE FROM $USER_KEYSPACE.$USER_TABLE WHERE $COLUMN_EMAIL = '$email' IF EXISTS")
    }

}
