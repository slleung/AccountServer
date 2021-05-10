import di.*
import io.grpc.Server
import io.grpc.ServerBuilder
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import services.AuthenticationService

private const val SERVER_PORT = 8080

// application entry point
fun main() {
    startKoin {
        // there is a problem with the koin logger, disabling
//        printLogger()

        modules(serviceModule, handlerModule, repositoryModule, dataStoreModule, daoModule)
    }

    val server = AuthenticationServer()
    server.start()

    Runtime.getRuntime().addShutdownHook(object : Thread() {
        override fun run() {
            server.shutdown()
        }
    })

    server.awaitTermination()
}

class AuthenticationServer : KoinComponent {

    private val authenticationService: AuthenticationService by inject()

    private val server: Server by lazy {
        ServerBuilder.forPort(SERVER_PORT) // TODO Enable TLS
//            .useTransportSecurity(certChainFile, privateKeyFile)
            .addService(authenticationService)
            .build()
    }

    fun start() {
        server.start()
    }

    fun shutdown() {
        server.shutdown()
    }

    fun awaitTermination() {
        server.awaitTermination()
    }

}
