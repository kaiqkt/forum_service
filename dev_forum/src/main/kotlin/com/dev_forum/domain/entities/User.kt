package com.dev_forum.domain.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class User(
        val name: String,
        val email: String,
        val password: String,
        @Id val id: String? = null
)