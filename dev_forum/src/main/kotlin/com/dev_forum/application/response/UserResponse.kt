package com.dev_forum.application.response

import com.dev_forum.domain.entities.User

data class UserResponse (
        val name: String?,
        val email: String?,
        val id: String?
) {
    companion object {
        fun toJson(user: User?): String {
            return ("{\"profile\": " +
                    "{\"id\": \"${user?.id}\", " +
                    "\"name\": \"${user?.name}\", " +
                    "\"email\": \"${user?.email}\" }" +
                    "}")
        }
    }
}