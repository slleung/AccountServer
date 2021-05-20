package data.source.datasource

import data.Error
import data.Result
import data.User
import data.source.datasource.dao.UserDao
import di.dataStoreModule
import io.mockk.coEvery
import io.mockk.mockkClass
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.junit5.KoinTestExtension
import org.koin.test.junit5.mock.MockProviderExtension
import org.koin.test.mock.declareMock
import java.util.*

@ExperimentalCoroutinesApi
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class DefaultUserDataStoreTest : KoinTest {

    @JvmField
    @RegisterExtension
    val koinTestExtension = KoinTestExtension.create {
        modules(dataStoreModule)
    }

    @JvmField
    @RegisterExtension
    val mockProvider = MockProviderExtension.create { clazz ->
        mockkClass(clazz)
    }

    private val defaultUserDataStore: UserDataStore by inject()

    @Test
    fun `Insert a new user`() = runBlockingTest {
        val newUser = User(
            UUID.fromString("123e4567-e89b-42d3-a456-556642440000"),
            "test@email.com",
            "passwordHash",
            Date(1621490432)
        )
        declareMock<UserDao> {
            coEvery { getUser("test@email.com") }.answers { null }
            coEvery { insertUser(newUser) }.answers { }
        }

        val result = defaultUserDataStore.insertUser(newUser)

        assertTrue(result is Result.Success, "Could not insert user.")
    }

    @Test
    fun `Insert a new user already exists`() = runBlockingTest {
        val newUser = User(
            UUID.fromString("123e4567-e89b-42d3-a456-556642440000"),
            "test@email.com",
            "passwordHash",
            Date(1621490432)
        )
        declareMock<UserDao> {
            coEvery { getUser("test@email.com") }.answers { newUser }
            coEvery { insertUser(newUser) }.answers { }
        }

        val result = defaultUserDataStore.insertUser(newUser)

        assertTrue(result is Result.Failure, "Should have failed but succeeded.")
        assertTrue((result as Result.Failure).error is Error.AlreadyExistsError, "Wrong error type. $result")
    }

    @Test
    fun getUser() {

    }

}
