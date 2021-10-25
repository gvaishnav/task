package com.task.task.model.req

import com.task.task.model.BitCoinHistory
import com.task.task.model.UserCoin
import java.util.*
import javax.persistence.*


data class PasswordReq(val userId: Long? = null,val password: String? = null)
