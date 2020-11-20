package com.dev_forum.domain.repositories

import com.dev_forum.domain.entities.PasswordResetToken
import org.springframework.data.mongodb.repository.MongoRepository

interface PasswordResetTokenRepository: MongoRepository<PasswordResetToken, String> {
    fun findByToken(token: String?): PasswordResetToken?
    fun deleteByToken(token: String?)
}