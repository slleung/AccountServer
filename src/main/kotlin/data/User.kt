package data

import com.datastax.driver.mapping.annotations.Column
import com.datastax.driver.mapping.annotations.PartitionKey
import com.datastax.driver.mapping.annotations.Table
import kotlinx.datetime.LocalDate

@Table(keyspace = "user_keyspace", name = "users")
data class User(
    @PartitionKey @Column(name = "email") val email: String,
    @Column(name = "password") val password: String,   // base64 encoded
    @Column(name = "creation_date") val creationDate: LocalDate,
    @Column(name = "last_login_date") val lastLoginDate: LocalDate
)
