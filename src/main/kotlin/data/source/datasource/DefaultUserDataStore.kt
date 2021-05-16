package data.source.datasource

import data.Error.AlreadyExistsError
import data.Error.GenericError
import data.Result
import data.source.datasource.dao.UserDao

class DefaultUserDataStore(private val userDao: UserDao) : UserDataStore {

    override suspend fun createUser(email: String, password: String): Result<Unit> {
        return try {
            val resultSet = userDao.createUser(email, password)

            if (resultSet.wasApplied()) {
                Result.Success(Unit)
            } else {
                Result.Failure(AlreadyExistsError("User already exists."))
            }
        } catch (e: Exception) {
            Result.Failure(GenericError(e))
        }
    }

}
