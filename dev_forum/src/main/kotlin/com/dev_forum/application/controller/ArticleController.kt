package com.dev_forum.application.controller

import com.dev_forum.application.dto.Author
import com.github.slugify.Slugify
import com.dev_forum.application.dto.NewArticle
import com.dev_forum.application.dto.UpdateArticle
import com.dev_forum.application.dto.UpdateUser
import com.dev_forum.application.response.Response
import com.dev_forum.application.validations.InvalidRequest
import com.dev_forum.domain.entities.Article
import com.dev_forum.domain.entities.Tag
import com.dev_forum.domain.repositories.ArticleRepository
import com.dev_forum.domain.repositories.TagRepository
import com.dev_forum.domain.service.ArticleService
import com.dev_forum.domain.service.UserService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/article")
class ArticleController(
        val tagRepository: TagRepository,
        val articleRepository: ArticleRepository,
        val userService: UserService,
        val articleService: ArticleService
) {

    @GetMapping()
    fun articles(@RequestParam(defaultValue = "20") limit: Int,
                 @RequestParam(defaultValue = "0") offset: Int,
                 @RequestParam(defaultValue = "") tag: String,
                 @RequestParam(defaultValue = "") author: String,
                 @RequestParam(defaultValue = "") favorited: String): ResponseEntity<Response<Any>> {
        val response: Response<Any> = Response<Any>()

        val p = PageRequest.of(offset, limit, Sort.Direction.DESC, "createdAt")
        val a = articleService.findBy(tag, author, favorited, p)

        response.data = (articlesView(a))

        return ResponseEntity.ok(response)
    }

    @GetMapping("/feed")
    fun feed(
            @RequestParam(defaultValue = "20") limit: Int,
            @RequestParam(defaultValue = "0") offset: Int
    ): ResponseEntity<Response<Any>> {
        val response: Response<Any> = Response<Any>()

        val currentUser = userService.currentUser()
        val page = articleRepository.findByAuthorIdInOrderByCreatedAtDesc(currentUser?.follows?.map { it.id },
                PageRequest.of(offset, limit))
        response.data = (articlesView(page))

        return ResponseEntity.ok(response)
    }

    @GetMapping("/{slug}")
    fun article(@PathVariable slug: String): ResponseEntity<Response<Any>>{
        val response: Response<Any> = Response<Any>()

        articleRepository.findBySlug(slug)?.let {
            response.data = articleView(it)
            return ResponseEntity.ok(response)
        }
        return ResponseEntity.notFound().build()
    }

    @PostMapping()
    fun newArticle(@Valid @RequestBody newArticle: NewArticle, result: BindingResult): ResponseEntity<Response<Any>> {
        val response: Response<Any> = Response<Any>()

        InvalidRequest.check(response, result)

        if (response.erros.isNotEmpty()) {
            return ResponseEntity.badRequest().body(response)
        }

        var slug = Slugify().slugify(newArticle.title)

        if (articleRepository.existsBySlug(slug)) {
            slug += "-" + UUID.randomUUID().toString().substring(0, 8)
        }

        val currentUser = userService.currentUser()

        val tagList = newArticle.tagList.map {
            tagRepository.findByName(it) ?: tagRepository.save(Tag(name = it))
        }

        val article = NewArticle.toDocument(newArticle, slug,
                Author(name = currentUser?.name,
                        email = currentUser?.email,
                        id = currentUser?.id,
                        image = currentUser?.image), tagList)

        articleRepository.save(article)
        response.data = (articleView(article))

        return ResponseEntity.ok().body(response)
    }

    @PutMapping("/{slug}")
    fun updateArticle(@PathVariable slug: String, @Valid @RequestBody article: UpdateArticle, result: BindingResult): ResponseEntity<Response<Any>> {
        val response: Response<Any> = Response<Any>()

        articleRepository.findBySlug(slug)?.let {
            val user = userService.currentUser()
            if (it?.author?.id != user?.id){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
            }

            var slug: String? = it.slug
            article.title?.let { newTitle ->
                if (newTitle != it?.title) {
                    slug = Slugify().slugify(article.title)
                    if (articleRepository.existsBySlug(slug!!)){
                        slug += "-" + UUID.randomUUID().toString().substring(0, 8)
                    }
                }
            }

            val tagList = article.tagList?.map {
                tagRepository.findByName(it) ?: tagRepository.save(Tag(name = it))
            }

            val update = it.copy(
                    slug = slug,
                    title = article.title ?: it.title,
                    description = article.description ?: it.description,
                    body = article.body ?: it.body,
                    updatedAt = LocalDateTime.now(),
                    tagList = if (tagList.isNullOrEmpty()) it.tagList else tagList.toMutableList()
            )

            articleRepository.save(update)
            response.data = articleView(update)
            return ResponseEntity.ok().body(response)
        }
        return ResponseEntity.notFound().build()
    }

    fun articleView(article: Article) = mapOf("article" to article)

    fun articlesView(articles: List<Article>) = mapOf("articles" to articles,
            "articlesCount" to articles.size)
}