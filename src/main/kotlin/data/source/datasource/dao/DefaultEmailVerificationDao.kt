package data.source.datasource.dao

import com.datastax.driver.core.Cluster
import com.datastax.driver.mapping.Mapper.Option.*
import com.datastax.driver.mapping.MappingManager
import data.EmailVerificationCode
import data.EmailVerificationCodeByEmail
import java.util.*

class DefaultEmailVerificationDao : EmailVerificationCodeDao {

    private val cluster by lazy {
        val clusterBuilder = Cluster.Builder()
        Configs.scyllaIps.forEach { ip ->
            clusterBuilder.addContactPoint(ip)
        }
        clusterBuilder.build()
    }

    private val session by lazy {
        cluster.connect()
    }

    private val mappingManager by lazy {
        MappingManager(session)
    }

    private val emailVerificationCodeMapper by lazy {
        mappingManager.mapper(EmailVerificationCode::class.java).apply {
            setDefaultSaveOptions(saveNullFields(false))
        }
    }

    private val emailVerificationCodeByEmailMapper by lazy {
        mappingManager.mapper(EmailVerificationCodeByEmail::class.java).apply {
            setDefaultSaveOptions(saveNullFields(false))
        }
    }

    override suspend fun insert(emailVerificationCode: EmailVerificationCode) {
        emailVerificationCodeMapper.save(emailVerificationCode, ifNotExists(true), ttl(getTTL(emailVerificationCode)))
    }

    override suspend fun get(id: UUID): EmailVerificationCode? {
        return emailVerificationCodeMapper.get(id)
    }

    override suspend fun get(email: String): EmailVerificationCode? {
        return emailVerificationCodeByEmailMapper.get(email)?.toEmailVerificationCode()
    }

    override suspend fun update(emailVerificationCode: EmailVerificationCode) {
        emailVerificationCodeMapper.save(emailVerificationCode, ttl(getTTL(emailVerificationCode)))
    }

    override suspend fun delete(id: UUID) {
        emailVerificationCodeMapper.delete(id)
    }

    override suspend fun delete(emailVerificationCode: EmailVerificationCode) {
        emailVerificationCodeMapper.delete(emailVerificationCode)
    }

    private fun getTTL(emailVerificationCode: EmailVerificationCode) : Int {
        val ttl = (emailVerificationCode.expirationDate.time - Date().time) / 1000;
        return if (ttl < 0) 0 else ttl.toInt()
    }

}