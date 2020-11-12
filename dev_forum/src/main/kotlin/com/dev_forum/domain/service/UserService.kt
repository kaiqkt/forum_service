package com.dev_forum.domain.service

import com.dev_forum.application.dto.UserRequest
import com.dev_forum.domain.repositories.UserRepository
import com.dev_forum.resources.security.UserDetailsImpl
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(private val repository: UserRepository, private val bCryptPasswordEncoder: BCryptPasswordEncoder){

    fun findByEmail(email: String?) = repository.findByEmail(email)

    fun save(user: UserRequest) {
        user.password = bCryptPasswordEncoder.encode(user.password)
        val document = UserRequest.toDocument(user)
        repository.save(document)
    }

    fun currentUser(): String? {
        val user = findByEmail(authenticated()?.username)

        return user?.id
    }

    private fun authenticated(): UserDetailsImpl? {
        return try {
            SecurityContextHolder.getContext().authentication.principal as UserDetailsImpl
        } catch (e: Exception) {
            null
        }
    }

}
