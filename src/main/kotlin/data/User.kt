package data

import Constants.COLUMN_CREATION_DATE
import Constants.COLUMN_EMAIL
import Constants.COLUMN_ID
import Constants.COLUMN_LAST_LOGIN_DATE
import Constants.COLUMN_PASSWORD_HASH
import Constants.COLUMN_VERIFICATION_STATE
import Constants.USER_BY_EMAIL_MV
import Constants.ACCOUNT_KEYSPACE
import Constants.USER_TABLE
import com.datastax.driver.mapping.annotations.Column
import com.datastax.driver.mapping.annotations.PartitionKey
import com.datastax.driver.mapping.annotations.Table
import java.util.*

@Table(keyspace = ACCOUNT_KEYSPACE, name = USER_TABLE)
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

@Table(keyspace = ACCOUNT_KEYSPACE, name = USER_BY_EMAIL_MV)
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
