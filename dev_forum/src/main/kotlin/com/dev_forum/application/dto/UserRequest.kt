package com.dev_forum.application.dto

import com.dev_forum.domain.entities.User
import com.dev_forum.domain.entities.enum.Profile
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty


data class UserRequest(
        @get:NotEmpty(message = "Name cannot be empty.")
        val name: String = "",

        @get:Email(message = "Invalid email.")
        val email: String = "",

        @get:NotEmpty(message = "Password cannot be empty.")
        var password: String = ""
) {
    companion object {

        fun toDocument(userRequest: UserRequest): User {
            return User(
                    name = userRequest.name,
                    email = userRequest.email,
                    password = userRequest.password,
                    profile = Profile.ROLE_USER
            )
        }
    }
}