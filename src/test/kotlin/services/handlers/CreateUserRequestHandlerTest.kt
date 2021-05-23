package services.handlers

import com.google.rpc.Code
import com.google.rpc.Status
import com.vmiforall.authentication.AuthenticationProto
import di.*
import helpers.initDb
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.junit5.KoinTestExtension

@ExperimentalCoroutinesApi
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class CreateUserRequestHandlerTest : KoinTest {

    @JvmField
    @RegisterExtension
    val koinTestExtension = KoinTestExtension.create {
        modules(handlerModule, repositoryModule, dataSourceModule, daoModule, dispatchersModule)
    }

    private val createUserRequestHandler : CreateUserRequestHandler by inject()

    @Test
    fun `Handle normal request`() = runBlockingTest {
        initDb()
        val request = AuthenticationProto.CreateUserRequest.newBuilder()
            .setEmail("test@email.com")
            .setPassword("password")
            .build()

        val actualResponse = createUserRequestHandler.handleRequest(request)

        val expectedResponse = AuthenticationProto.CreateUserResponse.newBuilder()
            .setStatus(
                Status.newBuilder()
                    .setCode(Code.OK.number)
                    .build()
            )
            .build()
        actualResponse shouldBe expectedResponse
    }

}
