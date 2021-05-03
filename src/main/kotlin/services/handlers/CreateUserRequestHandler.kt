package services.handlers

import com.vmiforall.authentication.AuthenticationProto
import io.jsonwebtoken.Jwt
import io.jsonwebtoken.Jwts
import org.bouncycastle.crypto.generators.SCrypt
import java.security.SecureRandom

class CreateUserRequestHandler :
    RequestHandler<AuthenticationProto.CreateUserRequest, AuthenticationProto.CreateUserResponse> {

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
        val secret = byteArrayOf(64)    // 64 * 8 = 512 bit
        secureRandom.nextBytes(secret)

        Jwts.builder()
            .setHeader(mapOf(
                "alg" to "HS256",
                "typ" to "jwt"
            ))
            .se

        return AuthenticationProto.CreateUserResponse.getDefaultInstance()
    }

}
