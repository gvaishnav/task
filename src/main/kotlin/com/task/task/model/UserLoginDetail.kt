package com.task.task.model


import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Table(name = "user_login_detail")
@Entity
open class UserLoginDetail{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    open var id: Long? = null


    @Column(name = "user_id", nullable = false, unique = true)
    open var userId: Long? = null


    @Column(name = "password", nullable = false)
    open var password: String? = null

    @Column(name = "created_at", nullable = false)
    open var createdAt: Date? = null
    @Column(name = "update_at", nullable = true)
    open var updatedAt: Date? = null

}