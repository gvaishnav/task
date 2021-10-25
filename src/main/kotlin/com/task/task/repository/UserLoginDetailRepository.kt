package com.task.task.repository;

import com.task.task.model.UserLoginDetail
import org.springframework.data.jpa.repository.JpaRepository

interface UserLoginDetailRepository : JpaRepository<UserLoginDetail, Long> {
    fun existsByUserId(userId: Long): Boolean
    fun findByUserId(userId: Long): UserLoginDetail
}