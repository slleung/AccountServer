package services

import com.vmiforall.authentication.AuthenticationProto
import com.vmiforall.authentication.AuthenticationServiceGrpcKt
import io.grpc.CallCredentials
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import services.handlers.AuthenticateUserRequestHandler
import services.handlers.CreateUserRequestHandler

class AuthenticationService(
    private val createUserRequestHandler: CreateUserRequestHandler,
    private val authenticateUserRequestHandler: AuthenticateUserRequestHandler
) : AuthenticationServiceGrpcKt.AuthenticationServiceCoroutineImplBase() {

    override suspend fun createUser(request: AuthenticationProto.CreateUserRequest): AuthenticationProto.CreateUserResponse {
        return createUserRequestHandler.handleRequest(request)
    }

    override suspend fun authenticateUser(request: AuthenticationProto.AuthenticateUserRequest): AuthenticationProto.AuthenticateUserResponse {
        return authenticateUserRequestHandler.handleRequest(request)
    }

}
