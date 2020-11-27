package com.dev_forum.domain.service

import com.dev_forum.domain.entities.Article
import com.dev_forum.domain.repositories.ArticleRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class ArticleService(val repository: ArticleRepository) {

    fun findBy(tag: String?, author: String?, favorited: String?, p: PageRequest): List<Article>{
        return when {
            !tag.isNullOrBlank() -> repository.findByTagListName(tag, p)
            !author.isNullOrBlank() -> repository.findByAuthorName(author, p)
            !favorited.isNullOrBlank() -> repository.findByFavorited(favorited, p)
            else -> repository.findAll(p).toList()
        }
    }
}