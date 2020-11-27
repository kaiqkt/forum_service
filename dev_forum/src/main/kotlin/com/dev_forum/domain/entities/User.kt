package com.dev_forum.domain.entities

import com.dev_forum.domain.entities.enum.Profile
import com.fasterxml.jackson.annotation.JsonIgnore
import org.bson.types.Binary
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class User(
        @Id val id: String? = null,
        val name: String?,
        val email: String?,
        val userName: String?,
        val bio: String?,
        val image: String? = null,
        @JsonIgnore
        var password: String?,
        @JsonIgnore
        val profile: Profile?,
        @JsonIgnore
        var follows: MutableList<User> = mutableListOf()
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