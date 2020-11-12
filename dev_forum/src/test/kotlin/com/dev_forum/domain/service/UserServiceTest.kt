package com.dev_forum.domain.service

import com.dev_forum.application.dto.UserRequest
import com.dev_forum.domain.entities.User
import com.dev_forum.domain.entities.enum.Profile
import com.dev_forum.domain.repositories.UserRepository
import io.mockk.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder


class UserServiceTest{

    private val repository = mockk<UserRepository>(relaxed = true)
    private val bcrypt = mockk<BCryptPasswordEncoder>(relaxed = true)
    private val service = UserService(repository, bcrypt)

    @Test
    fun `given a existing email, should return user information from the database`(){
        val email = "test@test.com"
        val document = User(
                name = "Edward",
                email = "test@test.com",
                password ="123456",
                profile = Profile.ROLE_USER
        )

        every { repository.findByEmail(email) } returns document

        val user = service.findByEmail(email)

        Assertions.assertEquals("Edward", user?.name)
        Assertions.assertEquals("test@test.com", user?.email)
        Assertions.assertEquals("123456", user?.password)
        Assertions.assertEquals(Profile.ROLE_USER, user?.profile)
    }

    @Test
    fun `given a non exist email when consult in database should return null`(){
        val email = "test@test.com"

        every { repository.findByEmail(email) } returns null

        Assertions.assertNull( service.findByEmail(email))
    }

    @Test
    fun `given a user request when save in database should return user document`(){
        val request = UserRequest(
                name = "Edward",
                email = "test@test.com",
                password = "123456"
        )

        every { repository.save(UserRequest.toDocument(request)) } just runs

        service.save(request)

        verify { repository.save(UserRequest.toDocument(request)) }
    }
}

