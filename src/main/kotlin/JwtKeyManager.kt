import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.io.File
import java.security.Key

private const val KEY_DIRECTORY = "/jwt"
private const val KEY_FILE_NAME = "jwt-key"
private val KEY_PATH = KEY_DIRECTORY + File.separator + KEY_FILE_NAME

/**
 * Manages Jwt keys.
 */
class JwtKeyManager {

    fun getSecretKey(): Key {

    }

    private fun generateKey(): String {
        val key = Keys.secretKeyFor(SignatureAlgorithm.HS256)
    }

    private fun hasKey() = File(KEY_PATH).exists()

    private fun readKey() : String {
        File(KEY_PATH).bufferedReader().use { bufferedReader ->
            return bufferedReader.readLine()
        }
    }

    private fun writeKey(base64Key: String) {
        File(KEY_DIRECTORY).mkdirs()

        File(KEY_PATH).bufferedWriter().use { bufferedWriter ->
            bufferedWriter.write(base64Key)
            bufferedWriter.flush()
        }
    }

}
