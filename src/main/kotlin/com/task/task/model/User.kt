package com.task.task.model

import java.util.*
import javax.persistence.*

@Table(name = "user")
@Entity
open class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id", nullable = false)
    open var userId: Long? = null

    @Column(name = "user_name", unique = false)
    open var userName: String? = null
    @Column(name = "mobile_number", unique = true)
    open var mobileNumber: String? = null


    @Column(name = "created_at", nullable = false)
    open var createdAt: Date? = null

    @Column(name = "updated_at")
    open var updatedAt: Date? = null

    @OneToMany(cascade = [CascadeType.ALL],fetch = FetchType.LAZY)
    @JoinColumn(name = "user_coins")
    open var userCoins: MutableList<UserCoin>? = null
    @OneToMany(cascade = [CascadeType.ALL],fetch = FetchType.LAZY)
    @JoinColumn(name = "bit_coins")
    open var bitCoinHistories: MutableList<BitCoinHistory>? = null

}