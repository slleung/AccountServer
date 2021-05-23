package di

import data.source.DefaultUserRepository
import data.source.UserRepository
import data.source.datasource.DefaultUserDataSource
import data.source.datasource.UserDataSource
import data.source.datasource.dao.DefaultUserDao
import data.source.datasource.dao.UserDao
import org.koin.core.qualifier.named
import org.koin.dsl.module
import services.AuthenticationService
import services.handlers.AuthenticateUserRequestHandler
import services.handlers.CreateUserRequestHandler

val serviceModule = module {
    single { AuthenticationService(get()) }
}

val handlerModule = module {
    single { CreateUserRequestHandler(get()) }
    single { AuthenticateUserRequestHandler(get()) }
}

val repositoryModule = module {
    single<UserRepository> { DefaultUserRepository(get(), get(qualifier = named(IO))) }
}

val dataSourceModule = module {
    single<UserDataSource> { DefaultUserDataSource(get()) }
}

val daoModule = module {
    single<UserDao> { DefaultUserDao() }
}
