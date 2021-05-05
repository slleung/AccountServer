package data.source

import data.Result
import data.User

interface UserRepository {

    suspend fun createUser(email: String, password: String) : Result<User>

}
