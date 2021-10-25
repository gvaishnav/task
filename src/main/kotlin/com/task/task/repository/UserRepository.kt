package com.task.task.repository;

import com.task.task.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun existsByMobileNumber(mobileNumber: String): Boolean
}