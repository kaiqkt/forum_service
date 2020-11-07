package com.dev_forum.application.controller

import com.dev_forum.application.dto.UserRequest
import com.dev_forum.application.response.Response
import com.dev_forum.domain.entities.User
import com.dev_forum.domain.service.UserService
import com.dev_forum.domain.validation.UserRecordValidation
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/user")
class UserController (val userService: UserService) {

    @PostMapping
    fun register(@Valid @RequestBody userRequest: UserRequest, result: BindingResult): ResponseEntity<String> {
        val response: Response<String> = Response<String>()

        val exist: User? = userService.findByEmail(userRequest.email)
        val res: BindingResult = UserRecordValidation.existingUser(exist?.email, result)

        if (res.hasErrors()){
            result.allErrors.forEach { erro ->
                erro.defaultMessage?.let { response.erros.add(it) }
            }
            return ResponseEntity.badRequest().body(response.erros.toString())
        }

        userService.save(userRequest)

        return ResponseEntity.ok().build()
    }

    @GetMapping
    fun a(): String {
        return "aa"
    }
}

@RestController
@RequestMapping("/test")
class A (val userService: UserService) {
    @GetMapping
    fun a(): String {
        return "aa"
    }
}