package com.dev_forum.application.dto

import javax.validation.constraints.NotEmpty

data class UpdateArticle(
        var title: String? = null,
        var description: String? = null,
        var body: String? = null,
        var tagList: List<String>? = null)