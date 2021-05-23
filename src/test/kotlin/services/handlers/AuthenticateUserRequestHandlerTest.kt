package services.handlers

import com.google.rpc.Code
import com.vmiforall.authentication.AuthenticationProto.AuthenticateUserRequest
import com.vmiforall.authentication.AuthenticationProto.AuthenticateUserResponse
import data.User
import di.*
import helpers.initDb
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.junit5.KoinTestExtension
import java.util.*

@ExperimentalCoroutinesApi
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class AuthenticateUserRequestHandlerTest : KoinTest {

    @JvmField
    @RegisterExtension
    val koinTestExtension = KoinTestExtension.create {
        modules(handlerModule, repositoryModule, dataSourceModule, daoModule, dispatchersModule)
    }

    private val authenticateUserRequestHandler: AuthenticateUserRequestHandler by inject()

    private val testUser1: User by lazy {
        User(
            UUID.fromString("c5813fd8-4453-48cb-af88-e5fadd127487"),
            "test@email.com",
            "X03WldjB4fUev5nC05RY7rcbjcDD8UkrCC+4NxRN2UemXgS6kx8oyHFcaF3ODs/6", // password: "password"
            Date(1621490432),
            Date(1621490432)
        )
    }

    @Test
    fun `Handle normal request`() = runBlockingTest {
        initDb(testUser1)
        val request = AuthenticateUserRequest.newBuilder()
            .setEmail(testUser1.email)
            .setPassword("password")
            .build()

        val response = authenticateUserRequestHandler.handleRequest(request)

        response.status.code shouldBe Code.OK_VALUE
    }
}