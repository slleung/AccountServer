package data.source

import data.Result
import data.User
import di.daoModule
import di.dataSourceModule
import di.dispatchersModule
import di.repositoryModule
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
internal class DefaultUserRepositoryTest : KoinTest {

    @JvmField
    @RegisterExtension
    val koinTestExtension = KoinTestExtension.create {
        modules(repositoryModule, dataSourceModule, daoModule, dispatchersModule)
    }

    private val defaultUserRepository: UserRepository by inject()

    private val testUser1 by lazy {
        User(
            UUID.fromString("123e4567-e89b-42d3-a456-556642440000"),
            "test@email.com",
            "passwordHash",
            Date(1621490432)
        )
    }

    @Test
    fun `Insert a user`() = runBlockingTest {
        initDb()

        val result = defaultUserRepository.insertUser(testUser1)

        result shouldBe instanceOf<Result.Success<Unit>>()
    }

    @Test
    fun `Get a user by id`() = runBlockingTest {
        initDb(testUser1)

        val result = defaultUserRepository.getUser(testUser1.id)

        result shouldBe instanceOf<Result.Success<User>>()
    }

    @Test
    fun `Get a user by email`() = runBlockingTest {
        initDb(testUser1)

        val result = defaultUserRepository.getUser(testUser1.email)

        result shouldBe instanceOf<Result.Success<User>>()
    }

}