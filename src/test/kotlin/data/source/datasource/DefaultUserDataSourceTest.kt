package data.source.datasource

import data.Error
import data.Result
import data.User
import di.daoModule
import di.dataSourceModule
import helpers.initDb
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.instanceOf
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
internal class DefaultUserDataSourceTest : KoinTest {

    @JvmField
    @RegisterExtension
    val koinTestExtension = KoinTestExtension.create {
        modules(dataSourceModule, daoModule)
    }

    private val defaultUserDataSource: UserDataSource by inject()

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

        val result = defaultUserDataSource.insertUser(testUser1)

        result shouldBe instanceOf<Result.Success<Unit>>()
    }

    @Test
    fun `Insert a new user already exist`() = runBlockingTest {
        initDb(testUser1)

        val result = defaultUserDataSource.insertUser(testUser1)

        result shouldBe instanceOf<Result.Failure>()
        (result as Result.Failure).error shouldBe instanceOf<Error.AlreadyExistsError>()
    }

    @Test
    fun `Get a user by id`() = runBlockingTest {
        initDb(testUser1)

        val result = defaultUserDataSource.getUser(testUser1.id)

        result shouldBe instanceOf<Result.Success<User>>()
        (result as Result.Success).data shouldBe testUser1
    }

    @Test
    fun `Get a user by id does not exist`() = runBlockingTest {
        initDb()

        val result = defaultUserDataSource.getUser(testUser1.id)

        result shouldBe instanceOf<Result.Failure>()
        (result as Result.Failure).error shouldBe instanceOf<Error.NotFoundError>()
    }

    @Test
    fun `Get a user by email`() = runBlockingTest {
        initDb(testUser1)

        val result = defaultUserDataSource.getUser(testUser1.email)

        result shouldBe instanceOf<Result.Success<User>>()
        (result as Result.Success).data shouldBe testUser1
    }

    @Test
    fun `Get a user by email does not exist`() = runBlockingTest {
        initDb()

        val result = defaultUserDataSource.getUser(testUser1.id)

        result shouldBe instanceOf<Result.Failure>()
        (result as Result.Failure).error shouldBe instanceOf<Error.NotFoundError>()
    }

}
