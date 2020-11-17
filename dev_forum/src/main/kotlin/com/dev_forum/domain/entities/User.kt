package com.dev_forum.domain.entities

import com.dev_forum.domain.entities.enum.Profile
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class User(
        val name: String,
        val email: String,
        @JsonIgnore
        val password: String,
        @JsonIgnore
        val profile: Profile,
        @Id val id: String? = null
) {
        companion object {
                fun toJson(user: User?): String {
                        return ("{\"user\": " +
                                "{\"id\": \"${user?.id}\", " +
                                "\"name\": \"${user?.name}\", " +
                                "\"email\": \"${user?.email}\" }" +
                                "}")
                }
        }
}