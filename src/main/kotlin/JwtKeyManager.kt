import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.io.Encoders
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

    private val keyAlgorithm = SignatureAlgorithm.HS512

    // cache the key to avoid unnecessary file IO
    private var cachedKey: Key? = null

    fun getJwtSecretKey(): Key {
        if (cachedKey != null) {
            return cachedKey as Key
        }

        if (hasStoredKey()) {
            cachedKey = readKeyFromStorage()
        } else {
            cachedKey = generateKey()
            writeKeyToStorage(cachedKey as Key)
        }

        return cachedKey as Key
    }

    private fun generateKey(): Key {
        return Keys.secretKeyFor(keyAlgorithm)
    }

    private fun hasStoredKey() = File(KEY_PATH).exists()

    private fun readKeyFromStorage(): Key {
        File(KEY_PATH).bufferedReader().use { bufferedReader ->
            return Keys.hmacShaKeyFor(Decoders.BASE64.decode(bufferedReader.readLine()))
        }
    }

    private fun writeKeyToStorage(key: Key) {
        File(KEY_DIRECTORY).mkdirs()

        File(KEY_PATH).bufferedWriter().use { bufferedWriter ->
            bufferedWriter.write(Encoders.BASE64.encode(key.encoded))
            bufferedWriter.flush()
        }
    }

}
