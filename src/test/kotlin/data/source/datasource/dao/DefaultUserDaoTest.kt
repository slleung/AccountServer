package data.source.datasource.dao

import com.datastax.driver.core.Cluster
import data.source.datasource.dao.DefaultUserDao.UserStore.USER_KEYSPACE
import data.source.datasource.dao.DefaultUserDao.UserStore.USER_TABLE
import data.source.datasource.dao.DefaultUserDao.UserStore.UserKeyspace.UserTable.COLUMN_EMAIL
import di.daoModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.core.context.startKoin

import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.junit5.KoinTestExtension
import kotlin.test.assertTrue

class DefaultUserDaoTest : KoinTest {

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
        startKoin {
            modules(daoModule)
        }
    }

    @AfterEach
    fun tearDown() {
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Create a user`() = runBlockingTest {
        val email = "test@email.com"
        val password = "password"   // pretend this is hashed

        userDao.createUser(email, password)

        assertTrue(userExistsInDatabase(email))
    }

    private fun userExistsInDatabase(email: String): Boolean {
        return session.execute("SELECT count(*) FROM $USER_KEYSPACE.$USER_TABLE WHERE $COLUMN_EMAIL = '$email'").count() == 1
    }

}
