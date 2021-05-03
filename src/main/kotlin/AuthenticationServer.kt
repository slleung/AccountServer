import di.handlerModule
import di.serviceModule
import io.grpc.Server
import io.grpc.ServerBuilder
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.logger.PrintLogger
import org.koin.core.component.inject
import services.AuthenticationService

private const val SERVER_PORT = 8080

// application entry point
fun main() {
    startKoin {
        logger(PrintLogger())

        modules(serviceModule, handlerModule)
    }

    val server = AuthenticationServer()
    server.start()
    server.awaitTermination()
}

class AuthenticationServer : KoinComponent {

    private val authenticationService : AuthenticationService by inject()

    private val server : Server by lazy {
        ServerBuilder.forPort(SERVER_PORT) // TODO Enable TLS
//            .useTransportSecurity(certChainFile, privateKeyFile)
            .addService(authenticationService)
            .build()
    }

    fun start() {
        server.start()
    }

    fun awaitTermination() {
        server.awaitTermination()
    }

}
