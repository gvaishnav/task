package com.task.task.model.req

import com.task.task.model.BitCoinHistory
import com.task.task.model.UserCoin
import java.util.*
import javax.persistence.*


data class BuyBitCoinReq(val userId: Long, val bitCoinRateId:String,val bitCoinAmount:Double)
