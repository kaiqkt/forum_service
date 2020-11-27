package com.dev_forum.application.dto

import javax.validation.constraints.Email

data class UpdateUser(
        val name: String? = null,
        val username: String? = null,
        val bio: String? = null,
        val image: String? = null,
        @get:Email(message = "Invalid email.")
        val email: String? = null,
        var password: String? = null
)