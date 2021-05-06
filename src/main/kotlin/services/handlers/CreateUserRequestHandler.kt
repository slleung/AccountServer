package services.handlers

import JwtKeyManager
import com.google.rpc.Code
import com.google.rpc.Status
import com.vmiforall.authentication.AuthenticationProto
import data.source.UserRepository
import io.jsonwebtoken.Jwts
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

        // TODO validate credentials

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
            .setJwtToken(jwtToken)
            .build()
    }

    fun validateEmail(email: String) : Boolean {

    }

}
