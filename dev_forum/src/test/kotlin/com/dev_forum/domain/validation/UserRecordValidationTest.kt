package com.dev_forum.domain.validation

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.ObjectError

class UserRecordValidationTest{
    private val validation = UserRecordValidation

    @Test
    fun `given a email, should return a error result`(){
        val email = "test@test.com"
        val result = BeanPropertyBindingResult(email, "email")

        val comp = BeanPropertyBindingResult(email, "email")
        comp.addError(ObjectError("user", "Email already use."))

        val res = validation.existingUser(email, result)

        Assertions.assertEquals(comp, res)
    }

    @Test
    fun `given a null email, should null result`(){
        val email = null
        val result = BeanPropertyBindingResult(email, "email")

        val comp = BeanPropertyBindingResult(email, "email")

        val res = validation.existingUser(email, result)

        Assertions.assertEquals(comp, res)
    }
}