package com.task.task.model

import lombok.Data
import lombok.NoArgsConstructor
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Data
@NoArgsConstructor
@Table(name = "user_coin")
@Entity
open class UserCoin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    open var id: Long? = null

    @Column(name = "coins", nullable = false)
    open var coins: Double? = null
    @Column(name = "userId", nullable = false)
    open var userId: Long? = null
    @Column(name = "created_at", nullable = false)
    open var createdAt: Date? = null
}