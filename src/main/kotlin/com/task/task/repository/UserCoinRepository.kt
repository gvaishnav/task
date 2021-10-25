package com.task.task.repository;

import com.task.task.model.UserCoin
import org.springframework.data.jpa.repository.JpaRepository

interface UserCoinRepository : JpaRepository<UserCoin, Long> {
}