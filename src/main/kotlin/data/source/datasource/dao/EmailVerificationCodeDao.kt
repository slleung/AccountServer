package data.source.datasource.dao

import data.EmailVerificationCode
import java.util.*

interface EmailVerificationCodeDao {

    suspend fun insert(emailVerificationCode: EmailVerificationCode)

    suspend fun get(id: UUID): EmailVerificationCode?

    suspend fun get(email: String): EmailVerificationCode?

    suspend fun update(emailVerificationCode: EmailVerificationCode)

    suspend fun delete(id: UUID)

    suspend fun delete(emailVerificationCode: EmailVerificationCode)

}
