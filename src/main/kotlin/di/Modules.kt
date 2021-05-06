package di

import data.source.DefaultUserRepository
import data.source.UserRepository
import data.source.datasource.DefaultUserDataStore
import data.source.datasource.UserDataStore
import data.source.datasource.dao.DefaultUserDao
import data.source.datasource.dao.UserDao
import org.koin.dsl.module
import services.AuthenticationService
import services.handlers.AuthenticateUserRequestHandler
import services.handlers.CreateUserRequestHandler

val serviceModule = module {
    single { AuthenticationService(get()) }
}

val handlerModule = module {
    single { CreateUserRequestHandler(userRepository = get()) }
    single { AuthenticateUserRequestHandler(userRepository = get()) }
}

val repositoryModule = module {
    single<UserRepository> { DefaultUserRepository(get()) }
}

val dataStoreModule = module {
    single<UserDataStore> { DefaultUserDataStore(get()) }
}

val daoModule = module {
    single<UserDao> { DefaultUserDao() }
}
