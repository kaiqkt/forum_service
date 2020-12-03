package com.dev_forum.domain.entities

import com.dev_forum.application.dto.Author
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.time.OffsetDateTime

@Document
data class Article(
        var slug: String? = "",
        var title: String? = "",
        var description: String? = "",
        var body: String? = "",
        //Lista de tags
        val tagList: MutableList<Tag> = mutableListOf(),
        var createdAt: LocalDateTime = LocalDateTime.now(),
        var updatedAt: LocalDateTime = LocalDateTime.now(),
        var comments: MutableList<Comment> = mutableListOf(),
        //Usuarios que curtiram o post
        var favorited: MutableList<User> = mutableListOf(),
        var author: Author,
        @JsonIgnore
        @Id
        var id: String? = null
) {
        fun favoritesCount() = favorited.size
}