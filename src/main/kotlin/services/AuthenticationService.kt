package services

import com.vmiforall.authentication.AuthenticationProto
import com.vmiforall.authentication.AuthenticationServiceGrpcKt

class AuthenticationService : AuthenticationServiceGrpcKt.AuthenticationServiceCoroutineImplBase() {

    override suspend fun createUser(request: AuthenticationProto.CreateUserRequest): AuthenticationProto.CreateUserResponse {
        return super.createUser(request)
    }

}
