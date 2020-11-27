package com.dev_forum.domain.service

import com.dev_forum.application.dto.UserRequest
import com.dev_forum.domain.entities.PasswordResetToken
import com.dev_forum.domain.entities.User
import com.dev_forum.domain.repositories.PasswordResetTokenRepository
import com.dev_forum.domain.repositories.UserRepository
import com.dev_forum.resources.security.UserDetailsImpl
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*


@Service
class UserService(private val repository: UserRepository,
                  private val bCryptPasswordEncoder: BCryptPasswordEncoder,
                  private val resetTokenRepository: PasswordResetTokenRepository
) {

    fun findByEmail(email: String?) = repository.findByEmail(email)

    fun save(user: UserRequest) {
        user.password = bCryptPasswordEncoder.encode(user.password)
        val document = UserRequest.toDocument(user)
        repository.save(document)
    }

    fun update(user: User?) {
        repository.save(user)
    }

    fun existsByEmail(email: String?) = repository.existsByEmail(email)

    fun currentUser() = findByEmail(authenticated()?.username)

    fun createPasswordResetTokenForUser(email: String, token: String) {
        val token = PasswordResetToken(token = token, email = email)
        resetTokenRepository.save(token)
    }

    fun validatePasswordResetToken(token: String?): String? {
        val user = resetTokenRepository.findByToken(token)
        return if (!isTokenFound(user)) "invalid token" else null
    }

    fun deleteToken(token: String?) {
        resetTokenRepository.deleteByToken(token)
    }

    fun findUserByToken(token: String?): User? {
        val resetToken = resetTokenRepository.findByToken(token)
        return repository.findByEmail(resetToken?.email)
    }
    fun changeUserPassword(user: User, password: String?) {
        user.password = bCryptPasswordEncoder.encode(password)
        repository.save(user)
    }

    private fun isTokenFound(passToken: PasswordResetToken?): Boolean {
        return passToken != null
    }

    private fun isTokenExpired(passToken: PasswordResetToken?): Boolean {
        val cal = Calendar.getInstance()
        return passToken?.expiryDate!!.before(cal.time)
    }

    private fun authenticated(): UserDetailsImpl? {
        return try {
            SecurityContextHolder.getContext().authentication.principal as UserDetailsImpl
        } catch (e: Exception) {
            null
        }
    }

}
