package data.source.datasource

import data.Error
import data.Result
import data.User
import data.source.datasource.dao.DefaultUserDao
import data.source.datasource.dao.UserDao
import di.daoModule
import di.dataStoreModule
import helpers.initDb
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get
import org.koin.test.inject
import org.koin.test.junit5.KoinTestExtension
import org.koin.test.junit5.mock.MockProviderExtension
import org.koin.test.mock.declare
import org.koin.test.mock.declareMock
import java.util.*

@ExperimentalCoroutinesApi
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class DefaultUserDataStoreTest : KoinTest {

    @JvmField
    @RegisterExtension
    val koinTestExtension = KoinTestExtension.create {
        modules(dataStoreModule, daoModule)
    }

    private val defaultUserDataStore : UserDataStore by inject()

    private val testUser1 by lazy {
        User(
            UUID.fromString("123e4567-e89b-42d3-a456-556642440000"),
            "test@email.com",
            "passwordHash",
            Date(1621490432)
        )
    }

    @Test
    fun `Insert a new user`() = runBlockingTest {
        initDb()

        val result = defaultUserDataStore.insertUser(testUser1)

        result.shouldBeTypeOf<Result.Success<User>>()
        assertTrue(result is Result.Success, "Could not insert user.")
    }

    @Test
    fun `Insert a new user already exist`() = runBlockingTest {
        initDb(testUser1)

        val result = defaultUserDataStore.insertUser(testUser1)

        assertTrue(result is Result.Failure, "Should have failed but succeeded.")
        assertTrue((result as Result.Failure).error is Error.AlreadyExistsError, "Wrong error type. $result")
    }

    @Test
    fun `Get a user by id`() {
        initDb()

    }

    @Test
    fun `Get a user by id does not exist`() {

    }

    @Test
    fun `Get a user by email`() {

    }

    @Test
    fun `Get a user by email does not exist`() {

    }

}
