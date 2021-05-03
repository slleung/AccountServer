package services

import com.vmiforall.authentication.AuthenticationProto
import com.vmiforall.authentication.AuthenticationServiceGrpcKt
import io.grpc.CallCredentials
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import services.handlers.CreateUserRequestHandler

class AuthenticationService : AuthenticationServiceGrpcKt.AuthenticationServiceCoroutineImplBase(), KoinComponent {

    private val createUserRequestHandler : CreateUserRequestHandler by inject()

    override suspend fun createUser(request: AuthenticationProto.CreateUserRequest): AuthenticationProto.CreateUserResponse {
        return createUserRequestHandler.handleRequest(request)
    }

}
