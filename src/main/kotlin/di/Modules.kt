package di

import data.source.DefaultUserRepository
import data.source.UserRepository
import data.source.datasource.DefaultUserDataSource
import data.source.datasource.UserDataSource
import data.source.datasource.dao.DefaultEmailVerificationDao
import data.source.datasource.dao.DefaultUserDao
import data.source.datasource.dao.EmailVerificationCodeDao
import data.source.datasource.dao.UserDao
import org.koin.core.qualifier.named
import org.koin.dsl.module
import services.AccountService
import services.handlers.AuthenticateUserRequestHandler
import services.handlers.CreateUserRequestHandler
import services.handlers.VerifyUserEmailRequestHandler

val serviceModule = module {
    single { AccountService(get(), get(), get()) }
}

val handlerModule = module {
    single { CreateUserRequestHandler(get()) }
    single { AuthenticateUserRequestHandler(get()) }
    single { VerifyUserEmailRequestHandler(get()) }
}

val repositoryModule = module {
    single<UserRepository> { DefaultUserRepository(get(), get(qualifier = named(IO))) }
}

val dataSourceModule = module {
    single<UserDataSource> { DefaultUserDataSource(get(), get()) }
}

val daoModule = module {
    single<UserDao> { DefaultUserDao() }
    single<EmailVerificationCodeDao> { DefaultEmailVerificationDao() }
}
