package data.source.datasource

import data.Error.*
import data.Error.GenericError
import data.Result
import data.User
import data.source.datasource.dao.UserDao
import java.util.*

class DefaultUserDataSource(
    private val userDao: UserDao,
) : UserDataSource {

    override suspend fun insert(user: User): Result<Unit> {
        try {
            if (userDao.get(user.email) != null) {
                return Result.Failure(AlreadyExistsError("Email already exists."))
            }

            userDao.insert(user)
            return Result.Success(Unit)
        } catch (e: Exception) {
            return Result.Failure(GenericError(e))
        }
    }

    override suspend fun get(id: UUID): Result<User> {
        return try {
            val user = userDao.get(id)

            if (user != null) {
                Result.Success(user)
            } else {
                Result.Failure(NotFoundError("User does not exist."))
            }
        } catch (e: Exception) {
            Result.Failure(GenericError(e))
        }
    }

    override suspend fun get(email: String): Result<User> {
        return try {
            val user = userDao.get(email)

            if (user != null) {
                Result.Success(user)
            } else {
                Result.Failure(NotFoundError("User does not exist."))
            }
        } catch (e: Exception) {
            Result.Failure(GenericError(e))
        }
    }

    override suspend fun update(user: User): Result<Unit> {
        try {
            if (userDao.get(user.id) == null) {
                return Result.Failure(NotFoundError("User does not exist."))
            }

            userDao.update(user)
            return Result.Success(Unit)
        } catch (e: Exception) {
            return Result.Failure(GenericError(e))
        }
    }

}
