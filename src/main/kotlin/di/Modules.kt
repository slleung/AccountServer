package di

import org.koin.dsl.module
import services.AuthenticationService

val authenticationServiceModule = module {
    single { AuthenticationService() }
}
