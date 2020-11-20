package com.dev_forum.application.response

data class Response<T> (
        var data: T?= null,
        var erros: ArrayList<String> = arrayListOf()
)