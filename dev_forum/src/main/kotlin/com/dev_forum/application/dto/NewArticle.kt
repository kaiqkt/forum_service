package com.dev_forum.application.dto

import com.dev_forum.domain.entities.Article
import com.dev_forum.domain.entities.Tag
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import javax.validation.constraints.NotEmpty

data class NewArticle(
        @get:NotEmpty(message = "title cannot be empty.")
        var title: String? = null,

        @get:NotEmpty(message = "description cannot be empty.")
        var description: String? = null,

        @get:NotEmpty(message = "body cannot be empty.")
        var body: String? = null,

        var tagList: List<String> = listOf()
) {
    companion object {
        fun dateFormat(): String {
            val a = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            return a.format(LocalDateTime.now())
        }

        fun toDocument(article: NewArticle, slug: String, author: Author, tagList: List<Tag>): Article {
            return Article(
                    slug = slug,
                    author = author,
                    title = article.title,
                    description = article.description,
                    body = article.body,
                    tagList = tagList.toMutableList()
            )
        }
    }
}