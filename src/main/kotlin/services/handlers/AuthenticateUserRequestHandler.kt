package services.handlers

import Configs
import Constants.JWT_CLAIM_USER_ID
import Constants.SALT_LEN
import Constants.SCRYPT_DKLEN
import Constants.SCRYPT_N
import Constants.SCRYPT_P
import Constants.SCRYPT_R
import com.google.protobuf.Timestamp
import com.google.rpc.Code
import com.google.rpc.Status
import com.vmiforall.account.AccountProto
import com.vmiforall.account.AccountProto.AuthenticateUserRequest
import com.vmiforall.account.AccountProto.AuthenticateUserResponse
import data.Result
import data.User
import data.source.UserRepository
import ext.logger
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import org.bouncycastle.crypto.generators.SCrypt
import java.util.*

class AuthenticateUserRequestHandler(
    private val userRepository: UserRepository
) : RequestHandler<AuthenticateUserRequest, AuthenticateUserResponse> {
    
    private val logger by logger()

    override suspend fun handleRequest(request: AuthenticateUserRequest): AuthenticateUserResponse {
        val email = request.email
        val password = request.password

        val result = userRepository.getUser(email)

        if (result is Result.Failure) {
            return incorrectEmailOrPasswordResponse()
        }

        val user = (result as Result.Success).data

        val savedPasswordHash = Decoders.BASE64.decode(user.passwordHash)
        val savedHash = savedPasswordHash.copyOfRange(0, SCRYPT_DKLEN)
        val savedSalt = savedPasswordHash.copyOfRange(SCRYPT_DKLEN, SCRYPT_DKLEN + SALT_LEN)

        val newHash = SCrypt.generate(
            password.toByteArray(),
            savedSalt,
            SCRYPT_N,
            SCRYPT_R,
            SCRYPT_P,
            SCRYPT_DKLEN
        )

        if (!newHash.contentEquals(savedHash)) {
            return incorrectEmailOrPasswordResponse()
        }

        val jwtToken = Jwts.builder()
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + Configs.jwtExpiration))
            .claim(JWT_CLAIM_USER_ID, user.id.toString())
            .signWith(Configs.jwtSecret)
            .compact()

        user.lastLoginDate = Date()
        val updateLastLoginDateResult = userRepository.updateUser(user)
        if (updateLastLoginDateResult is Result.Failure) {
            logger.error("Cannot update last login date for user ${user.id}")
        }

        return successResponse(jwtToken, user)
    }

    private fun incorrectEmailOrPasswordResponse(): AuthenticateUserResponse {
        return AuthenticateUserResponse.newBuilder()
            .setStatus(
                Status.newBuilder()
                    .setCode(Code.INVALID_ARGUMENT_VALUE)
                    .setMessage("Incorrect email or password")
                    .build()
            )
            .build()
    }

    private fun failureResponse(result: Result.Failure): AuthenticateUserResponse {
        return AuthenticateUserResponse.newBuilder()
            .setStatus(
                Status.newBuilder()
                    .setCode(result.error.code)
                    .setMessage(result.error.message)
                    .build()
            )
            .build()
    }

    private fun successResponse(jwt: String, user: User): AuthenticateUserResponse {
        return AuthenticateUserResponse.newBuilder()
            .setStatus(
                Status.newBuilder()
                    .setCode(Code.OK_VALUE)
                    .build()
            )
            .setJwtToken(jwt)
            .setUser(
                AccountProto.User.newBuilder()
                    .setId(user.id.toString())
                    .setEmail(user.email)
                    .setCreationDate(
                        Timestamp.newBuilder()
                            .setSeconds(user.creationDate.time / 1000)
                            .setNanos(((user.creationDate.time % 1000) * 1000000).toInt())
                            .build()
                    )
                    .setLastLoginDate(
                        Timestamp.newBuilder()
                            .setSeconds(user.lastLoginDate.time / 1000)
                            .setNanos(((user.lastLoginDate.time % 1000) * 1000000).toInt())
                            .build()
                    )
                    .build()
            )
            .build()
    }

}
