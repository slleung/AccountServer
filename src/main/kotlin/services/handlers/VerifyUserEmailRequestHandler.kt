package services.handlers

import com.vmiforall.account.AccountProto.VerifyUserEmailRequest
import com.vmiforall.account.AccountProto.VerifyUserEmailResponse
import data.source.UserRepository

class VerifyUserEmailRequestHandler(
    private val userRepository: UserRepository
) : RequestHandler<VerifyUserEmailRequest, VerifyUserEmailResponse> {

    override suspend fun handleRequest(request: VerifyUserEmailRequest): VerifyUserEmailResponse {
        return VerifyUserEmailResponse.getDefaultInstance()
    }

}
