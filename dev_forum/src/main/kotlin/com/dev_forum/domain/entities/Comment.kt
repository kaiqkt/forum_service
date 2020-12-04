package com.dev_forum.domain.entities

import com.dev_forum.application.dto.Author
import java.time.LocalDateTime

data class Comment(var createdAt: String = formatter.format(LocalDateTime.now()),
                   var updatedAt: String = formatter.format(LocalDateTime.now()),
                   var body: String = "",
                   var author: Author,
                   var id: String)