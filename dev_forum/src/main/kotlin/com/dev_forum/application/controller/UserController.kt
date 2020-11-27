package com.dev_forum.application.controller

import com.dev_forum.application.dto.Author
import com.dev_forum.application.dto.UpdateUser
import com.dev_forum.application.dto.UserRequest
import com.dev_forum.application.response.Response
import com.dev_forum.application.validations.InvalidRequest
import com.dev_forum.domain.entities.Article
import com.dev_forum.domain.entities.User
import com.dev_forum.domain.service.UserService
import com.dev_forum.resources.security.JWTUtil
import com.fasterxml.jackson.annotation.JsonIgnore
import org.bson.BsonBinarySubType
import org.bson.types.Binary
import org.springframework.data.annotation.Id
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.validation.Valid

@RestController
@RequestMapping("/user")
class UserController(val service: UserService, val jwt: JWTUtil) {


    @PostMapping
    fun register(@Valid @RequestBody userRequest: UserRequest, result: BindingResult): ResponseEntity<Response<Any>> {
        val response: Response<Any> = Response<Any>()

        checkUserAvailability(userRequest?.username, userRequest?.email, result)
        InvalidRequest.check(response, result)

        if (response.erros.isNotEmpty()) {
            return ResponseEntity.badRequest().body(response)
        }
        service.save(userRequest)

        return ResponseEntity.ok().build()
    }

    @GetMapping
    fun currentUser(): ResponseEntity<Response<Map<String, Author?>>> {
        val response: Response<Map<String, Author?>> = Response<Map<String, Author?>>()
        val u = service.currentUser()
        response.data = (view(Author(u?.name, u?.email, u?.id)))
        return ResponseEntity.ok().body(response)
    }

    @PutMapping
    fun updateUser(@Valid @RequestBody user: UpdateUser, result: BindingResult): ResponseEntity<Response<Any>> {
        val response: Response<Any> = Response<Any>()

        val currentUser = service.currentUser()

        checkUserAvailability(user?.username, user?.email, result)
        InvalidRequest.check(response, result)

        if (response.erros.isNotEmpty()) {
            return ResponseEntity.badRequest().body(response)
        }

        checkUpdateUser(currentUser, user, result)
        InvalidRequest.check(response, result)

        val u = currentUser?.copy(
                name = user.name ?: currentUser.name,
                email = user?.email ?: currentUser.email,
                image = user?.image ?: currentUser.image,
                bio = user?.bio ?: currentUser.bio,
                password = newPassword(user?.password) ?: currentUser.password,
                profile = currentUser.profile,
                id = currentUser.id
        )

        service.update(u)
        response.data = (viewUpdate(u))
        val token = jwt.generateToken(u?.email)

        return ResponseEntity.ok().header("Authorization", "$token").body(response);
    }

    private fun viewUpdate(user: User?) = mapOf("user" to user)
    private fun view(user: Author?) = mapOf("user" to user)

    private fun checkUserAvailability(u: String?, e: String?, result: BindingResult): BindingResult {
        e?.let { email ->
            if (service.existsByEmail(email)) {
                result.addError(ObjectError("user", "Email already use."))
                return result
            }
        }
        u?.let { email ->
            if (service.existsByUserName(email)) {
                result.addError(ObjectError("user", "Username already use."))
                return result
            }
        }
        return result
    }

    private fun checkUpdateUser(currentUser: User?, user: UpdateUser, result: BindingResult) {
        if (currentUser?.email != user.email && user.email != null) {
            if (service.existsByEmail(user.email!!)) {
                result.addError(FieldError("", "email", "already taken"))
            }
        }
        if (user.password == "") {
            result.addError(FieldError("", "password", "can't be empty"))
        }
    }

    private fun newPassword(password: String?): String? {
        return if (password != null) {
            BCrypt.hashpw(password, BCrypt.gensalt())
        } else {
            null
        }
    }
}