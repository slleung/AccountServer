package data.source.datasource

import data.EmailVerificationCode
import data.Result
import java.util.*

interface EmailVerificationCodeDataSource {

    suspend fun insert(emailVerificationCode: EmailVerificationCode): Result<Unit>

    suspend fun get(id: UUID): Result<EmailVerificationCode>

    suspend fun get(email: String): Result<EmailVerificationCode>

    suspend fun update(emailVerificationCode: EmailVerificationCode): Result<Unit>

    suspend fun delete(id: UUID): Result<Unit>

    suspend fun delete(emailVerificationCode: EmailVerificationCode): Result<Unit>

}
