package data.source

import data.Result
import data.User

interface UserRepository {

    suspend fun insertUser(user: User): Result<Unit>

}
