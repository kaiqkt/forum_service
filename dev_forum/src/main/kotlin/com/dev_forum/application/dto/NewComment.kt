package com.dev_forum.application.dto

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class NewComment (
    @NotNull(message = "can't be missing")
    @Size(min = 1, message = "can't be empty")
    var body: String? = ""
)