package data

import com.datastax.driver.mapping.annotations.Column
import com.datastax.driver.mapping.annotations.PartitionKey
import com.datastax.driver.mapping.annotations.Table
import java.util.*

const val USER_KEYSPACE = "user_keyspace"
const val USER_TABLE = "users"

const val COLUMN_UUID = "uuid"
const val COLUMN_EMAIL = "email"
const val COLUMN_PASSWORD = "password"
const val COLUMN_CREATION_DATE = "creation_date"
const val COLUMN_LAST_LOGIN_DATE = "email"

@Table(keyspace = USER_KEYSPACE, name = USER_TABLE)
data class User(
    @PartitionKey @Column(name = COLUMN_UUID) val uuid: UUID,
    @Column(name = COLUMN_EMAIL) val email: String,
    @Column(name = COLUMN_PASSWORD) val password: String,   // base64 encoded
    @Column(name = COLUMN_CREATION_DATE) val creationDate: Date,
    @Column(name = COLUMN_LAST_LOGIN_DATE) val lastLoginDate: Date
)
