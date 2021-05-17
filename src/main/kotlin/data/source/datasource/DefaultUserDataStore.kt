package data.source.datasource

import data.Error.AlreadyExistsError
import data.Error.GenericError
import data.Result
import data.User
import data.source.datasource.dao.UserDao
import data.source.datasource.dao.UserDao.UserStore.UserKeyspace.UserTable.COLUMN_CREATION_DATE
import data.source.datasource.dao.UserDao.UserStore.UserKeyspace.UserTable.COLUMN_EMAIL
import data.source.datasource.dao.UserDao.UserStore.UserKeyspace.UserTable.COLUMN_PASSWORD
import kotlinx.datetime.LocalDate

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

    override suspend fun getUser(email: String): Result<User> {
        return try {
            val resultSet = userDao.getUser(email)

            if (resultSet.count() != 0) {
                val row = resultSet.one()
                val email = row.getString(COLUMN_EMAIL)
                val password = row.getString(COLUMN_PASSWORD)
                val creationDate = LocalDate() row.getTimestamp(COLUMN_CREATION_DATE)
                val lastLoginDate = row.getTimestamp(COLUMN_CREATION_DATE)

                val user = User(email, password, creationDate, lastLoginDate)
            }
            resultSet.
        } catch (e: Exception) {
            Result.Failure(GenericError(e))
        }
    }
}
