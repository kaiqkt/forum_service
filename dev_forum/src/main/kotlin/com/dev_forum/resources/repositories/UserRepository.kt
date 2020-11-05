package com.dev_forum.resources.repositories

import com.dev_forum.domain.entities.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository: MongoRepository<User, String> {
    fun findByEmail(email: String): User?
}