import com.typesafe.config.ConfigFactory
import io.github.config4k.extract
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import java.io.File
import java.security.Key
import javax.crypto.SecretKey

/**
 * Application configs.
 */
object Configs {

    private val config by lazy {
        ConfigFactory.parseFile(File("server.conf"))
    }

    val jwtSecret: SecretKey
        get() {
            val secretBase64 = config.extract<String>("jwt.secret")
            val secret = Decoders.BASE64.decode(secretBase64)
            return Keys.hmacShaKeyFor(secret)
        }
    val jwtExpiration get() = config.extract<Long>("jwt.expiration")

    val scyllaIps get() = config.extract<List<String>>("scylla.ips")
    val scyllaDatacenters get() = config.extract<Map<String, Int>>("scylla.datacenters")
}
