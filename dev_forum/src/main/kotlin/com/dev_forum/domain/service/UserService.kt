package com.dev_forum.domain.service

import com.dev_forum.application.dto.UserRequest
import com.dev_forum.domain.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val repository: UserRepository){

    fun findByEmail(email: String) = repository.findByEmail(email)
    fun save(user: UserRequest) =
            repository.save(UserRequest.toDocument(user))
}