package com.dev_forum.application.dto

import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty


data class UserRequest(
        @get:NotEmpty(message = "Name cannot be empty.")
        val name: String= "",

        @get:Email(message = "Invalid email.")
        val email: String = "",

        @get:NotEmpty(message = "Password cannot be empty.")
        val password: String = ""
)