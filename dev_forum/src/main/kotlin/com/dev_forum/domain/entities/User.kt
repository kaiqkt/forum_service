package com.dev_forum.domain.entities

import com.dev_forum.domain.entities.enum.Profile
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class User(
        val name: String,
        val email: String,
        val password: String,
        val profile: Profile,
        @Id val id: String? = null
)