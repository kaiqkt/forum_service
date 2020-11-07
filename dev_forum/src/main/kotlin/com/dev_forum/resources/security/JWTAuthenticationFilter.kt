package com.dev_forum.resources.security

import com.dev_forum.application.dto.LoginDTO
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.IOException
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTAuthenticationFilter(jwtUtil: JWTUtil, authenticationManager: AuthenticationManager) : UsernamePasswordAuthenticationFilter() {
    private val jwtUtil: JWTUtil

    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        return try {
            val creds = ObjectMapper().readValue(request.inputStream, LoginDTO::class.java)
            val authToken = UsernamePasswordAuthenticationToken(creds.email, creds.password, ArrayList())
            println(authToken.authorities)
            authenticationManager.authenticate(authToken)
        } catch (e: IOException) {
            throw RuntimeException()
        }
    }

    @Throws(IOException::class, ServletException::class)
    override fun successfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain, authResult: Authentication) {
        val username: String = (authResult.principal as UserDetailsImpl).getUsername()
        val token = jwtUtil.generateToken(username)
        response.addHeader("Authorization", "Bearer $token")
        response.addHeader("access-control-expose-headers", "Authorization")
    }

    private inner class JWTAuthenticationFailureHandler : AuthenticationFailureHandler {
        @Throws(IOException::class, ServletException::class)
        override fun onAuthenticationFailure(request: HttpServletRequest, response: HttpServletResponse, exception: AuthenticationException) {
            response.status = 401
            response.contentType = "application/json"
            response.writer.append(json())
        }

        private fun json(): String {
            val date = Date().time
            return ("{\"timestamp\": " + date + ", "
                    + "\"status\": 401, "
                    + "\"error\": \"Não autorizado\", "
                    + "\"message\": \"Email ou senha inválidos\", "
                    + "\"path\": \"/login\"}")
        }
    }

    init {
        setAuthenticationFailureHandler(JWTAuthenticationFailureHandler())
        this.jwtUtil = jwtUtil
        this.authenticationManager = authenticationManager
    }
}