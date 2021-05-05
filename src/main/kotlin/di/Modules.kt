package di

import data.source.DefaultUserRepository
import data.source.UserRepository
import org.koin.dsl.module
import services.AuthenticationService
import services.handlers.CreateUserRequestHandler

val serviceModule = module {
    single { AuthenticationService(get()) }
}

val handlerModule = module {
    single { CreateUserRequestHandler(userRepository = get()) }
}

val repositoryModule = module {
    single<UserRepository> { DefaultUserRepository() }
}
