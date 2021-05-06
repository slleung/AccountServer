package services.handlers

import com.vmiforall.authentication.AuthenticationProto
import data.source.UserRepository

class AuthenticateUserRequestHandler(
    private val userRepository: UserRepository
) : RequestHandler<AuthenticationProto.AuthenticateUserRequest, AuthenticationProto.AuthenticateUserResponse {

    override suspend fun handleRequest(request: AuthenticationProto.AuthenticateUserRequest): AuthenticationProto.AuthenticateUserResponse {

    }
}
