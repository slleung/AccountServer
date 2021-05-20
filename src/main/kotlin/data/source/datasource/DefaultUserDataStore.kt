package data.source.datasource

import data.Error.*
import data.Error.GenericError
import data.Result
import data.User
import data.source.datasource.dao.UserDao
import java.util.*

class DefaultUserDataStore(private val userDao: UserDao) : UserDataStore {

    override suspend fun insertUser(user: User): Result<Unit> {
        try {
            if (userDao.getUser(user.email) != null) {
                return Result.Failure(AlreadyExistsError("Email already exists."))
            }

            userDao.insertUser(user)
            return Result.Success(Unit)
        } catch (e: Exception) {
            return Result.Failure(GenericError(e))
        }
    }

    override suspend fun getUser(id: UUID): Result<User> {
        return try {
            val user = userDao.getUser(id)

            if (user != null) {
                Result.Success(user)
            } else {
                Result.Failure(NotFoundError("User does not exist."))
            }
        } catch (e: Exception) {
            Result.Failure(GenericError(e))
        }
    }

    override suspend fun getUser(email: String): Result<User> {
        return try {
            val user = userDao.getUser(email)

            if (user != null) {
                Result.Success(user)
            } else {
                Result.Failure(NotFoundError("User does not exist."))
            }
        } catch (e: Exception) {
            Result.Failure(GenericError(e))
        }
    }

}
