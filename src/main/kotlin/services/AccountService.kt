package services

import com.vmiforall.account.AccountProto.*
import com.vmiforall.account.AccountServiceGrpcKt
import services.handlers.AuthenticateUserRequestHandler
import services.handlers.CreateUserRequestHandler
import services.handlers.VerifyUserEmailRequestHandler

class AccountService(
    private val createUserRequestHandler: CreateUserRequestHandler,
    private val authenticateUserRequestHandler: AuthenticateUserRequestHandler,
    private val verifyUserEmailRequestHandler: VerifyUserEmailRequestHandler
) : AccountServiceGrpcKt.AccountServiceCoroutineImplBase() {

    override suspend fun createUser(request: CreateUserRequest): CreateUserResponse {
        return createUserRequestHandler.handleRequest(request)
    }

    override suspend fun authenticateUser(request: AuthenticateUserRequest): AuthenticateUserResponse {
        return authenticateUserRequestHandler.handleRequest(request)
    }

    override suspend fun verifyUserEmail(request: VerifyUserEmailRequest): VerifyUserEmailResponse {
        return verifyUserEmailRequestHandler.handleRequest(request)
    }

}
