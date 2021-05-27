package data.source.datasource.dao

import data.User
import di.daoModule
import helpers.existsInDb
import helpers.initDb
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.junit5.KoinTestExtension
import java.util.*

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

    private val testUser1 by lazy {
        User(
            UUID.fromString("00000000-0000-0000-0000-000000000001"),
            "testUser1@email.com",
            "testUser1",
            Date(1621490432)
        )
    }

    @Test
    fun `Create a user`() = runBlockingTest {
        initDb()

        defaultUserDao.insert(testUser1)

        testUser1 should existsInDb()
    }

    @Test
    fun `Create a user already exists`() = runBlockingTest {
        initDb(testUser1)

        defaultUserDao.insert(testUser1)

        testUser1 should existsInDb()
    }

    @Test
    fun `Get a user by id`() = runBlockingTest {
        initDb(testUser1)

        val actualUser = defaultUserDao.get(testUser1.id)

        val expectedUser = testUser1
        actualUser shouldBe expectedUser
    }

    @Test
    fun `Get a user by email`() = runBlockingTest {
        initDb(testUser1)

        val actualUser = defaultUserDao.get(testUser1.email)

        val expectedUser = testUser1
        actualUser shouldBe expectedUser
    }

    @Test
    fun `Update a user`() = runBlockingTest {
        initDb()

        defaultUserDao.update(testUser1)    // upsert

        testUser1 should existsInDb()
    }

}
