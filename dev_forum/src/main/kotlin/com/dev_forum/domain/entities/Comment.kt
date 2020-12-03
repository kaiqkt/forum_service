package com.dev_forum.domain.entities

import com.dev_forum.application.dto.Author
import java.time.OffsetDateTime

data class Comment(var createdAt: OffsetDateTime = OffsetDateTime.now(),
                   var updatedAt: OffsetDateTime = OffsetDateTime.now(),
                   var body: String = "",
                   var author: Author,
                   var id: String)