package com.task.task.model

data class Pagination(
    var currentPage: Int?=0,
    var perPage: Int?=0,
    var totalRecords: Int?=0
)