package services.handlers

import Configs
import com.vmiforall.authentication.AuthenticationProto
import data.source.UserRepository
import io.jsonwebtoken.Jwts
import java.util.*

class AuthenticateUserRequestHandler(
    private val userRepository: UserRepository
) : RequestHandler<AuthenticationProto.AuthenticateUserRequest, AuthenticationProto.AuthenticateUserResponse> {

    override suspend fun handleRequest(request: AuthenticationProto.AuthenticateUserRequest): AuthenticationProto.AuthenticateUserResponse {

        val jwtToken = Jwts.builder()
            .setIssuedAt(Date())
            .setExpiration(Date(Configs.jwtExpiration))
            .signWith(Configs.jwtSecret)
            .compact()

        return AuthenticationProto.AuthenticateUserResponse.getDefaultInstance()
    }

}
