package com.dev_forum.application.controller

import com.dev_forum.application.dto.UserRequest
import com.dev_forum.application.response.Response
import com.dev_forum.application.validations.InvalidRequest
import com.dev_forum.domain.entities.User
import com.dev_forum.domain.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import kotlin.collections.ArrayList

@RestController
@RequestMapping("/user")
class UserController(val service: UserService) {

    @PostMapping
    fun register(@Valid @RequestBody userRequest: UserRequest, result: BindingResult): ResponseEntity<ArrayList<String>> {
        val response: Response<String> = Response<String>()

        checkUserAvailability(userRequest.email, result)
        InvalidRequest.check(response, result)

        if (response.erros.isNotEmpty()) {
            return ResponseEntity.badRequest().body(response.erros)
        }
        service.save(userRequest)

        return ResponseEntity.ok().build()
    }

    @GetMapping
    fun currentUser(): ResponseEntity<Map<String, User?>> {
        val response: Response<Map<String, User?>> = Response<Map<String, User?>>()
        response.data = view(service.currentUser())
        return ResponseEntity.ok().body(response.data)
    }

    private fun view(user: User?) = mapOf("user" to user)

    private fun checkUserAvailability(email: String, result: BindingResult): BindingResult {
        email.let { email ->
            if (service.existsByEmail(email)) {
                result.addError(ObjectError("user", "Email already use."))
                return result
            }
        }
        return result
    }
}