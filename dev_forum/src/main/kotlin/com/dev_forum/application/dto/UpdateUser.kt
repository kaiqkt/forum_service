package com.dev_forum.application.dto

import javax.validation.constraints.Email

data class UpdateUser(
        val name: String? = null,
        @get:Email(message = "Invalid email.")
        val email: String? = null,
        var password: String? = null
)