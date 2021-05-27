package data.source.datasource

import data.EmailVerificationCode
import data.Error
import data.Result
import data.source.datasource.dao.EmailVerificationCodeDao
import java.util.*

class DefaultEmailVerificationCodeDataSource(
    private val emailVerificationCodeDao: EmailVerificationCodeDao
) : EmailVerificationCodeDataSource {

    override suspend fun insert(emailVerificationCode: EmailVerificationCode): Result<Unit> {
        try {
            if (emailVerificationCodeDao.get(emailVerificationCode.email) != null) {
                return Result.Failure(Error.AlreadyExistsError("Email verification code for email ${emailVerificationCode.email} already exists."))
            }

            emailVerificationCodeDao.insert(emailVerificationCode)
            return Result.Success(Unit)
        } catch (e: Exception) {
            return Result.Failure(Error.GenericError(e))
        }
    }

    override suspend fun get(id: UUID): Result<EmailVerificationCode> {
        return try {
            val emailVerificationCode = emailVerificationCodeDao.get(id)

            if (emailVerificationCode != null) {
                Result.Success(emailVerificationCode)
            } else {
                Result.Failure(Error.NotFoundError("Email verification code for id $id does not exist."))
            }
        } catch (e: Exception) {
            Result.Failure(Error.GenericError(e))
        }
    }

    override suspend fun get(email: String): Result<EmailVerificationCode> {
        return try {
            val emailVerificationCode = emailVerificationCodeDao.get(email)

            if (emailVerificationCode != null) {
                Result.Success(emailVerificationCode)
            } else {
                Result.Failure(Error.NotFoundError("Email verification code for email $email does not exist."))
            }
        } catch (e: Exception) {
            Result.Failure(Error.GenericError(e))
        }
    }

    override suspend fun update(emailVerificationCode: EmailVerificationCode): Result<Unit> {
        try {
            if (emailVerificationCodeDao.get(emailVerificationCode.id) == null) {
                return Result.Failure(Error.NotFoundError("User does not exist."))
            }

            emailVerificationCodeDao.update(emailVerificationCode)
            return Result.Success(Unit)
        } catch (e: Exception) {
            return Result.Failure(Error.GenericError(e))
        }
    }

    override suspend fun delete(id: UUID): Result<Unit> {
        return try {
            emailVerificationCodeDao.delete(id)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(Error.GenericError(e))
        }
    }

    override suspend fun delete(emailVerificationCode: EmailVerificationCode): Result<Unit> {
        return delete(emailVerificationCode.id)
    }
}
