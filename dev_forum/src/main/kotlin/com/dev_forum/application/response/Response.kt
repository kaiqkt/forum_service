package com.dev_forum.application.response

data class Response<T> (
        val erros: ArrayList<String> = arrayListOf(),
        var data: T?= null
)