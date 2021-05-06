package services.handlers

import JwtKeyManager
import com.google.rpc.Code
import com.google.rpc.Status
import com.vmiforall.authentication.AuthenticationProto
import data.source.UserRepository
import io.jsonwebtoken.Jwts
import org.apache.commons.validator.routines.EmailValidator
import org.bouncycastle.crypto.generators.SCrypt
import java.security.SecureRandom
import java.util.*

class CreateUserRequestHandler(
    private val userRepository: UserRepository
) : RequestHandler<AuthenticationProto.CreateUserRequest, AuthenticationProto.CreateUserResponse> {

    private val jwtTokenExpiration = 15 * 60 * 1000L // 15 minutes in milliseconds

    override suspend fun handleRequest(request: AuthenticationProto.CreateUserRequest): AuthenticationProto.CreateUserResponse {
        val email = request.email
        val password = request.password

        if (!validateEmail(email)) {
            return AuthenticationProto.CreateUserResponse.newBuilder()
                .setStatus(
                    Status.newBuilder()
                        .setCode(Code.INVALID_ARGUMENT_VALUE)
                        .setMessage("Invalid email")
                )
                .build()
        }

        if (!validatePassword(password)) {
            return AuthenticationProto.CreateUserResponse.newBuilder()
                .setStatus(
                    Status.newBuilder()
                        .setCode(Code.INVALID_ARGUMENT_VALUE)
                        .setMessage("Invalid password")
                )
                .build()
        }

        // generate salt
        val secureRandom = SecureRandom()
        val salt = byteArrayOf(16)  // 16 * 8 = 128 bit
        secureRandom.nextBytes(salt)

        // Parameters taken from: https://cryptobook.nakov.com/mac-and-key-derivation/scrypt
        val hash = SCrypt.generate(password.toByteArray(), salt, 16384, 8, 1, 32)

        // TODO save the hash in the db

        // now we give the user a JWT token
        val jwtSecretKey = JwtKeyManager().getJwtSecretKey()

        val jwtToken = Jwts.builder()
            .setIssuedAt(Date())
            .setExpiration(Date(jwtTokenExpiration))
            .signWith(jwtSecretKey)
            .compact()

        return AuthenticationProto.CreateUserResponse.newBuilder()
            .setStatus(
                Status.newBuilder()
                    .setCode(Code.OK.number)
            )
            .build()
    }

    private fun validateEmail(email: String): Boolean {
        return EmailValidator.getInstance().isValid(email)
    }

    private fun validatePassword(password: String): Boolean {
        // according to NIST, password policy should be very loose so the user does not have difficulty creating passwords
        if (password.length < 8 || password.length > 128) {
            return false
        }
        return true
    }

}
