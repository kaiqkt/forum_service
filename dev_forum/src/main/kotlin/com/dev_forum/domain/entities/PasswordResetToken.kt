package com.dev_forum.domain.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
data class PasswordResetToken(
    @Id val id: String? = null,
    val token: String,
    val email: String,
    val expiryDate: Date? = null
)

