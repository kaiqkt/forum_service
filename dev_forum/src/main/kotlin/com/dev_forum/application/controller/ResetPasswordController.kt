package com.dev_forum.application.controller

import com.dev_forum.application.dto.PasswordDTO
import com.dev_forum.application.response.Response
import com.dev_forum.domain.service.UserService
import com.dev_forum.resources.mail.EmailService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/reset")
class ResetPasswordController(val userService: UserService, val emailService: EmailService) {

    @PostMapping()
    fun reset(@RequestParam("email") email: String): ResponseEntity<Response<String>> {
        val response: Response<String> = Response<String>()

        val user = userService.findByEmail(email)

        if (user == null) {
            response.erros = arrayListOf("User not found by this email: $email")
            return ResponseEntity.unprocessableEntity().body(response)
        }

        val token = UUID.randomUUID().toString()
        userService.createPasswordResetTokenForUser(email, token)

        val model = mapOf<String, String>(
                "name" to email,
                "token" to token
        )

        emailService.sendSimpleMessage(email, model)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/change")
    fun new(@RequestBody passwordDto: PasswordDTO): ResponseEntity<Response<String>> {
        val response: Response<String> = Response<String>()

        val result = userService.validatePasswordResetToken(passwordDto.token)
        val user = userService.findUserByToken(passwordDto.token)

        if (result != null) {
            response.erros = arrayListOf("Token invalid $result.")
            return ResponseEntity.unprocessableEntity().body(response)
        }

        return if (user != null) {
            userService.changeUserPassword(user, passwordDto.password)
            userService.deleteToken(passwordDto.token)
            ResponseEntity.ok().build()
        } else {
            response.erros = arrayListOf("Failed in change password.")
            ResponseEntity.badRequest().body(response)
        }

        return ResponseEntity.ok().build()
    }

}
