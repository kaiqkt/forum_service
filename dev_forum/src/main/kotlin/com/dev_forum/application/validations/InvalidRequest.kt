package com.dev_forum.application.validations

import com.dev_forum.application.response.Response
import com.dev_forum.domain.entities.User
import org.springframework.validation.BindingResult

object InvalidRequest {
    fun check(response: Response<Any>, result: BindingResult): Response<Any> {

        if (result.hasErrors()){
            result.allErrors.forEach { error ->
                error.defaultMessage?.let { response.erros.add(it) }
            }
            return response
        }
        return response
    }
}