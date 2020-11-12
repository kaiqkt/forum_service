package com.dev_forum.domain.service

import com.dev_forum.domain.entities.User
import com.dev_forum.domain.entities.enum.Profile
import com.dev_forum.domain.repositories.UserRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.core.userdetails.UsernameNotFoundException


class UserDetailsServiceImplTest{
    private val repository = mockk<UserRepository>(relaxed = true)
    private val service = UserDetailsServiceImpl(repository)

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

        val user = service.loadUserByUsername(email)

        Assertions.assertEquals("test@test.com", user.username)
        Assertions.assertEquals("123456", user.password)
        Assertions.assertEquals("[ROLE_USER]", user.authorities.toString())
        Assertions.assertEquals(true, user.isEnabled)
        Assertions.assertEquals(true, user.isAccountNonExpired)
        Assertions.assertEquals(true, user.isAccountNonLocked)
    }

    @Test
    fun `given a non exist email, should throw exception`(){
        val email = "test@test.com"

        every { repository.findByEmail(email) } returns null

        assertThrows<UsernameNotFoundException> {
            service.loadUserByUsername(email)
        }
    }
}