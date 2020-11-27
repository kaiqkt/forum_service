package com.dev_forum.domain.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Tag(val name: String = "",
               @Id
               @JsonIgnore
               var id: String? = null )