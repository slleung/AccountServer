package data

import com.datastax.driver.mapping.annotations.Column
import com.datastax.driver.mapping.annotations.PartitionKey
import com.datastax.driver.mapping.annotations.Table
import java.util.*

const val USER_KEYSPACE = "user_keyspace"
const val USER_TABLE = "users"
const val USER_BY_EMAIL_MV = "users_by_email"

const val COLUMN_ID = "id"
const val COLUMN_EMAIL = "email"
const val COLUMN_PASSWORD_HASH = "password_hash"
const val COLUMN_CREATION_DATE = "creation_date"
const val COLUMN_LAST_LOGIN_DATE = "last_login_date"

@Table(keyspace = USER_KEYSPACE, name = USER_TABLE)
data class User(
    @PartitionKey @Column(name = COLUMN_ID) var id: UUID = UUID.randomUUID(),
    @Column(name = COLUMN_EMAIL) var email: String = "",
    @Column(name = COLUMN_PASSWORD_HASH) var passwordHash: String = "",   // base64 encoded
    @Column(name = COLUMN_CREATION_DATE) var creationDate: Date = Date(),
    @Column(name = COLUMN_LAST_LOGIN_DATE) var lastLoginDate: Date = creationDate
)

private interface UserMV {
    fun toUser(): User
}

@Table(keyspace = USER_KEYSPACE, name = USER_BY_EMAIL_MV)
data class UserByEmail(
    @PartitionKey @Column(name = COLUMN_EMAIL) var email: String = "",
    @Column(name = COLUMN_ID) var id: UUID = UUID.randomUUID(),
    @Column(name = COLUMN_PASSWORD_HASH) var passwordHash: String = "",   // base64 encoded
    @Column(name = COLUMN_CREATION_DATE) var creationDate: Date = Date(),
    @Column(name = COLUMN_LAST_LOGIN_DATE) var lastLoginDate: Date = creationDate
) : UserMV {
    override fun toUser() = User(id, email, passwordHash, creationDate, lastLoginDate)
}
