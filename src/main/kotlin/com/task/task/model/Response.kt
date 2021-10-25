package com.task.task.model

data class Response(
    var message: String?=null,
    var data: Any?=null,
    var pagination: Pagination?=null,
    var success: Boolean?=false
)