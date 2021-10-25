package com.task.task.repository;

import com.task.task.model.BitCoinHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface BitCoinHistoryRepository : JpaRepository<BitCoinHistory, String> {
    @Query(value = "select * from bit_coin_history bch WHERE created_at>= :startDate and created_at<= :endDate  group by hour (created_at)", nativeQuery = true)
    fun findUserHistory(startDate: Date, endDate: Date):List<BitCoinHistory>
}