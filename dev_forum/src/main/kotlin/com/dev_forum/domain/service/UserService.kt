package com.dev_forum.domain.service

import com.dev_forum.application.dto.UserRequest
import com.dev_forum.domain.repositories.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(private val repository: UserRepository, private val bCryptPasswordEncoder: BCryptPasswordEncoder){

    fun findByEmail(email: String) = repository.findByEmail(email)

    fun save(user: UserRequest) {
        user.password = bCryptPasswordEncoder.encode(user.password)
        val document = UserRequest.toDocument(user)
        repository.save(document)
    }
}