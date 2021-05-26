
object Constants {
    const val SCRYPT_N = 16384
    const val SCRYPT_R = 8
    const val SCRYPT_P = 1
    const val SCRYPT_DKLEN = 32

    const val SALT_LEN = 16

    const val JWT_CLAIM_USER_ID = "userId"

    const val ACCOUNT_KEYSPACE = "account"
    const val USER_TABLE = "user"
    const val USER_BY_EMAIL_MV = "user_by_email"
    const val EMAIL_VERIFICATION_CODE_TABLE = "email_verification_code"
    const val EMAIL_VERIFICATION_CODE_BY_EMAIL_MV = "email_verification_code_by_email"

    const val COLUMN_ID = "id"
    const val COLUMN_EMAIL = "email"
    const val COLUMN_PASSWORD_HASH = "password_hash"
    const val COLUMN_CREATION_DATE = "creation_date"
    const val COLUMN_LAST_LOGIN_DATE = "last_login_date"
    const val COLUMN_VERIFICATION_STATE = "verification_state"
    const val COLUMN_VERIFICATION_CODE = "verification_code"
    const val COLUMN_EXPIRATION_DATE = "expiration_date"
}
