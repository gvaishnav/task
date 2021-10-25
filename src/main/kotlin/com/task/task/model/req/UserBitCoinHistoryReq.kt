package com.task.task.model.req

import com.task.task.model.BitCoinHistory
import com.task.task.model.UserCoin
import java.util.*
import javax.persistence.*


data class UserBitCoinHistoryReq(val userId: Long ,val startDateTime: String,val endDateTime:String,val timeZone:String)
