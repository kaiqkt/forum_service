package com.dev_forum.application.controller

import com.dev_forum.application.dto.UserRequest
import com.dev_forum.domain.entities.User
import com.dev_forum.domain.entities.enum.Profile
import com.dev_forum.domain.service.UserService
import com.dev_forum.domain.validation.UserRecordValidation
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.validation.BeanPropertyBindingResult

class UserControllerTest {

    private val service = mockk<UserService>(relaxed = true)
    private val controller = UserController(userService = service)
    @Test
    fun `given a user request, when a email not used should status 200`() {

        val request = UserRequest(
                name = "Edward",
                email = "test@test.com",
                password ="123456"
        )
        val result = BeanPropertyBindingResult(request, "userRequest")

        every { service.findByEmail(request.email) } returns null

        val response = controller.register(request, result)

        assertEquals(HttpStatus.OK, response.statusCode)
    }

    @Test
    fun `given a user request, when a email not used should status 404`() {

        val request = UserRequest(
                name = "Edward",
                email = "test@test.com",
                password ="123456"
        )

        val exists = User(
                name = "Edward",
                email = "test@test.com",
                password ="123456",
                profile = Profile.ROLE_USER
        )

        val result = BeanPropertyBindingResult(request, "userRequest")

        every { service.findByEmail(request.email) } returns exists

        val response = controller.register(request, result)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }
}

