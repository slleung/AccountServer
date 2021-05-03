package di

import org.koin.dsl.module
import services.AuthenticationService
import services.handlers.CreateUserRequestHandler

val serviceModule = module {
    single { AuthenticationService() }
}

val handlerModule = module {
    single { CreateUserRequestHandler() }
}
