package com.dev_forum.application.dto

import com.dev_forum.domain.entities.User
import com.dev_forum.domain.entities.enum.Profile
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty


data class UserRequest(

        @get:NotEmpty(message = "Name cannot be empty.")
        val name: String? = null,

        @get:NotEmpty(message = "Username cannot be empty.")
        val username: String? = null,

        @get:NotEmpty(message = "Bio cannot be empty.")
        val bio: String? = null,

        @get:NotEmpty(message = "Email cannot be empty.")
        @get:Email(message = "Invalid email.")
        val email: String? = null,

        @get:NotEmpty(message = "Password cannot be empty.")
        var password: String? = null
) {
    companion object {

        fun toDocument(userRequest: UserRequest): User {
            return User(
                    name = userRequest.name,
                    email = userRequest.email,
                    bio = userRequest.bio,
                    userName = userRequest.username,
                    password = userRequest.password,
                    profile = Profile.ROLE_USER
            )
        }
    }
}