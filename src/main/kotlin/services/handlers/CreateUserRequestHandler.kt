package services.handlers

import com.google.rpc.Code
import com.google.rpc.Status
import com.vmiforall.authentication.AuthenticationProto
import data.source.UserRepository
import io.jsonwebtoken.io.Encoders
import org.apache.commons.validator.routines.EmailValidator
import org.bouncycastle.crypto.generators.SCrypt
import java.security.SecureRandom

class CreateUserRequestHandler(
    private val userRepository: UserRepository
) : RequestHandler<AuthenticationProto.CreateUserRequest, AuthenticationProto.CreateUserResponse> {

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
        val hash = SCrypt.generate(password.toByteArray(), salt, Constants.SCRYPT_N, Constants.SCRYPT_R, Constants.SCRYPT_P, Constants.SCRYPT_DKLEN)

        val user = userRepository.createUser(email, Encoders.BASE64.encode(hash))

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
