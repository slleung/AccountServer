package data

import Constants.EMAIL_VERIFICATION_CODE_TABLE
import Constants.ACCOUNT_KEYSPACE
import Constants.COLUMN_CREATION_DATE
import Constants.COLUMN_EMAIL
import Constants.COLUMN_EXPIRATION_DATE
import Constants.COLUMN_ID
import Constants.COLUMN_VERIFICATION_CODE
import Constants.EMAIL_VERIFICATION_CODE_BY_EMAIL_MV
import com.datastax.driver.mapping.annotations.Column
import com.datastax.driver.mapping.annotations.PartitionKey
import com.datastax.driver.mapping.annotations.Table
import java.util.*

@Table(keyspace = ACCOUNT_KEYSPACE, name = EMAIL_VERIFICATION_CODE_TABLE)
data class EmailVerificationCode(
    @PartitionKey @Column(name = COLUMN_ID) var id: UUID = UUID.randomUUID(),
    @Column(name = COLUMN_EMAIL) var email: String = "",
    @Column(name = COLUMN_VERIFICATION_CODE) var verificationCode: String = "",
    @Column(name = COLUMN_CREATION_DATE) var creationDate: Date = Date(),
    @Column(name = COLUMN_EXPIRATION_DATE) var expirationDate: Date = creationDate
)


private interface EmailVerificationCodeMV {
    fun toEmailVerificationCode(): EmailVerificationCode
}

@Table(keyspace = ACCOUNT_KEYSPACE, name = EMAIL_VERIFICATION_CODE_BY_EMAIL_MV)
data class EmailVerificationCodeByEmail(
    @PartitionKey @Column(name = COLUMN_EMAIL) var email: String = "",
    @Column(name = COLUMN_ID) var id: UUID = UUID.randomUUID(),
    @Column(name = COLUMN_VERIFICATION_CODE) var verificationCode: String = "",
    @Column(name = COLUMN_CREATION_DATE) var creationDate: Date = Date(),
    @Column(name = COLUMN_EXPIRATION_DATE) var expirationDate: Date = creationDate
) : EmailVerificationCodeMV {
    override fun toEmailVerificationCode() =
        EmailVerificationCode(id, email, verificationCode, creationDate, expirationDate)
}
