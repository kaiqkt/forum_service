package com.dev_forum.domain.validation

import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError

object UserRecordValidation{
    
    fun existingUser(email: String?,  result: BindingResult): BindingResult {
        if (email != null) {
            result.addError(ObjectError("user", "Email already use."))
            return result
        }
        return result;
    }
}