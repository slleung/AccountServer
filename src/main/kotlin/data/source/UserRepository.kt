package data.source

import data.Result

interface UserRepository {

    suspend fun createUser(email: String, password: String): Result<Unit>

}
