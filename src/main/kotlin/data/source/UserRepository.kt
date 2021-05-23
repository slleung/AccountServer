package data.source

import data.Result
import data.User
import java.util.*

interface UserRepository {

    suspend fun insertUser(user: User): Result<Unit>

    suspend fun getUser(id: UUID): Result<User>

    suspend fun getUser(email: String): Result<User>

}
