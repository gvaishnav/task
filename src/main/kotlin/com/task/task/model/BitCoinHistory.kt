package com.task.task.model

import lombok.Data
import lombok.NoArgsConstructor
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Data
@NoArgsConstructor
@Table(name = "bit_coin_history")
@Entity
open class BitCoinHistory{
    @Id
    @Column(name = "id", nullable = false)
    open var id: String? = null

    @Column(name = "rate", nullable = false)
    open var rate: Long = 1

    @Column(name = "created_at",nullable = false)
    open var createdAt: Date? = null
}