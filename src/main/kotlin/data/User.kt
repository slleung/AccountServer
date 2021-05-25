package data

import com.datastax.driver.mapping.annotations.Column
import com.datastax.driver.mapping.annotations.PartitionKey
import com.datastax.driver.mapping.annotations.Table
import java.util.*

const val USER_KEYSPACE = "user_keyspace"
const val USER_TABLE = "user"
const val USER_BY_EMAIL_MV = "users_by_email"
const val USER_EMAIL_VERIFICATION_CODE_TABLE = "user_email_verification_code"

const val COLUMN_ID = "id"
const val COLUMN_EMAIL = "email"
const val COLUMN_PASSWORD_HASH = "password_hash"
const val COLUMN_CREATION_DATE = "creation_date"
const val COLUMN_LAST_LOGIN_DATE = "last_login_date"
const val COLUMN_VERIFICATION_STATE = "verification_state"
const val COLUMN_VERIFICATION_CODE = "verification_code"
const val COLUMN_EXPIRATION_DATE = "expiration_date"

@Table(keyspace = USER_KEYSPACE, name = USER_TABLE)
data class User(
    @PartitionKey @Column(name = COLUMN_ID) var id: UUID = UUID.randomUUID(),
    @Column(name = COLUMN_EMAIL) var email: String = "",
    @Column(name = COLUMN_PASSWORD_HASH) var passwordHash: String = "",   // base64 encoded
    @Column(name = COLUMN_CREATION_DATE) var creationDate: Date = Date(),
    @Column(name = COLUMN_LAST_LOGIN_DATE) var lastLoginDate: Date = creationDate,
    @Column(name = COLUMN_VERIFICATION_STATE) var verificationState: String = VerificationState.Unverified.name
)

enum class VerificationState {
    Unverified,
    EmailVerified
}

private interface UserMV {
    fun toUser(): User
}

@Table(keyspace = USER_KEYSPACE, name = USER_BY_EMAIL_MV)
data class UserByEmail(
    @PartitionKey @Column(name = COLUMN_EMAIL) var email: String = "",
    @Column(name = COLUMN_ID) var id: UUID = UUID.randomUUID(),
    @Column(name = COLUMN_PASSWORD_HASH) var passwordHash: String = "",   // base64 encoded
    @Column(name = COLUMN_CREATION_DATE) var creationDate: Date = Date(),
    @Column(name = COLUMN_LAST_LOGIN_DATE) var lastLoginDate: Date = creationDate,
    @Column(name = COLUMN_VERIFICATION_STATE) var verificationState: String = VerificationState.Unverified.name
) : UserMV {
    override fun toUser() = User(id, email, passwordHash, creationDate, lastLoginDate, verificationState)
}

@Table(keyspace = USER_KEYSPACE, name = USER_EMAIL_VERIFICATION_CODE_TABLE)
data class UserEmailVerificationCode(
    @PartitionKey @Column(name = COLUMN_ID) var id: UUID = UUID.randomUUID(),
    @Column(name = COLUMN_EMAIL) var email: String = "",
    @Column(name = COLUMN_VERIFICATION_CODE) var verificationCode: String = "",
    @Column(name = COLUMN_CREATION_DATE) var creationDate: Date = Date(),
    @Column(name = COLUMN_EXPIRATION_DATE) var expirationDate: Date = creationDate
)
