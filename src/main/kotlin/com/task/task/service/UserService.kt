package com.task.task.service

import com.task.task.model.Response
import com.task.task.model.User
import com.task.task.model.UserCoin
import com.task.task.model.UserLoginDetail
import com.task.task.model.req.*
import com.task.task.repository.BitCoinHistoryRepository
import com.task.task.repository.UserCoinRepository
import com.task.task.repository.UserLoginDetailRepository
import com.task.task.repository.UserRepository
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigInteger
import java.security.MessageDigest
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


@Service
class UserService {
    @Autowired
    internal lateinit var modelMapper:ModelMapper
    @Autowired
    internal lateinit var userRepository: UserRepository
    @Autowired
    internal lateinit var userLoginDetailRepository: UserLoginDetailRepository
    @Autowired
    internal lateinit var bitCoinHistoryRepository: BitCoinHistoryRepository
    @Autowired
    internal lateinit var userCoinRepository: UserCoinRepository


    fun saveNewUser(userReq: UserReq): Response {
        var response=Response()
         var user = modelMapper.map(userReq, User::class.java)
        user.createdAt= Date()
        user?.mobileNumber?.let {
            if (userRepository.existsByMobileNumber(it) ){
                response.message="mobile already exist"
                response.success=false
                return response
            }
            val result = userRepository.saveAndFlush(user)
            result ?.let {
                result.userId?.let {
                    response.message="Success"
                    response.success=true
                    response.data=result
                    return response
                }
            }
        }
        response.message="fail"
        response.success=false
        return response
    }


    fun saveUserPassword(passwordReq: PasswordReq): Response {
        var response=Response()
        passwordReq?.let {
            passwordReq.userId?.let {
                if (userRepository.existsById(passwordReq.userId)){
                    passwordReq.password?.let {
                        var loginUserDetail=modelMapper.map(passwordReq,UserLoginDetail::class.java)
                        if (userLoginDetailRepository.existsByUserId(passwordReq.userId)){
                            var loginDetail = userLoginDetailRepository.findByUserId(passwordReq.userId)
                            loginDetail.password=md5Hash(passwordReq.password)
                            loginDetail.updatedAt=Date()
                            userLoginDetailRepository.saveAndFlush(loginUserDetail)
                            response.success=true
                            response.message="successfully update password"
                            return response
                        }else{
                            loginUserDetail.createdAt= Date()
                            loginUserDetail.password=md5Hash(passwordReq.password)
                            userLoginDetailRepository.saveAndFlush(loginUserDetail)
                            response.success=true
                            response.message="successfully save password"
                            return response
                        }
                    }
                }
            }
        }

        response.message="fail"
        response.success=false
        return response
    }

    fun md5Hash(str: String): String {
        val md = MessageDigest.getInstance("MD5")
        val bigInt = BigInteger(1, md.digest(str.toByteArray(Charsets.UTF_8)))
        return String.format("%032x", bigInt)
    }

    fun buyBitCoin(buyBitCoinReq: BuyBitCoinReq): Response {
        val response=Response()
        if (userRepository.existsById(buyBitCoinReq.userId) && bitCoinHistoryRepository.existsById(buyBitCoinReq.bitCoinRateId)){
            val bitCoinHistory = bitCoinHistoryRepository.findById(buyBitCoinReq.bitCoinRateId).get()
            if (buyBitCoinReq.bitCoinAmount>=bitCoinHistory.rate){
                val coins = buyBitCoinReq.bitCoinAmount.div(bitCoinHistory.rate)
                val userCoin=UserCoin()
                userCoin.coins=coins
                userCoin.userId=buyBitCoinReq.userId
                userCoin.createdAt=Date()
                val res = userCoinRepository.saveAndFlush(userCoin)
                val userRes = userRepository.findById(buyBitCoinReq.userId).get()
                if (userRes.bitCoinHistories.isNullOrEmpty()){
                    userRes.bitCoinHistories=mutableListOf(bitCoinHistory)
                }else{
                    userRes.bitCoinHistories?.add(bitCoinHistory)
                }
                if (userRes.userCoins.isNullOrEmpty()){
                    userRes.userCoins=mutableListOf(res)
                }else{
                    userRes.userCoins?.add(res)
                }
                userRepository.saveAndFlush(userRes);
                response.message="Successfully add coins"
                response.success=true
                response.data=res
                return response
            }else{
                response.message="you need to add more amount in your account"
                response.success=false
                return response
            }

        }
        response.message="invalid request"
        response.success=false
        return response
    }

    fun getUserBitCoinHistory(userBitCoinHistoryReq: UserBitCoinHistoryReq): Response {
        val response=Response()
        if (userRepository.existsById(userBitCoinHistoryReq.userId)){
            val user = userRepository.findById(userBitCoinHistoryReq.userId).get()
            if (!user.userCoins.isNullOrEmpty() && !user.bitCoinHistories.isNullOrEmpty()){
                var startDate:Date?=null
                var endDate:Date?=null
                 try {
                     startDate= getAnyTimeZoneToUtcTimeZoneDate(userBitCoinHistoryReq.startDateTime,userBitCoinHistoryReq.timeZone)
                     if(startDate<user?.userCoins?.get(0)?.createdAt){
                         startDate=user?.userCoins?.get(0)?.createdAt
                     }
                     endDate= getAnyTimeZoneToUtcTimeZoneDate(userBitCoinHistoryReq.endDateTime,userBitCoinHistoryReq.timeZone)
                 }catch (e:Exception){
                     response.message=e.message
                     response.success=false
                   return response
                 }

                val res = bitCoinHistoryRepository.findUserHistory(startDate!!,endDate)
                var mutableList:MutableList<UserBtcHistoryRes>?=null
                var coin = 0.0
                user?.userCoins?.get(0)?.coins?.let {
                    coin=it
                    user?.userCoins?.removeAt(0)
                }


                res.forEach { it->
                    if (user?.userCoins?.isNullOrEmpty() != true){
                        if (user?.userCoins?.get(0)?.createdAt!!<=it.createdAt){
                            coin= user?.userCoins?.get(0)?.coins?.let { it1 -> coin.plus(it1) }!!
                            user?.userCoins?.removeAt(0)
                        }
                    }
                    val userBtcHistoryRes=UserBtcHistoryRes()
                    userBtcHistoryRes.amount=it.rate*coin
                    try {
                        it.createdAt?.let { date ->
                            userBtcHistoryRes.datetime=getUtcTimeZoneToAnyTimeZoneDate(date,userBitCoinHistoryReq.timeZone)
                        }
                    }catch (e:Exception){
                    }
                    if (mutableList.isNullOrEmpty()){
                        mutableList = mutableListOf(userBtcHistoryRes)
                    }else{
                        mutableList?.add(userBtcHistoryRes)
                    }
                }
                response.data=mutableList
                response.success=true
                response.message="Success"
                return response
            }
            response.message="coins data not found"
            response.success=false
            return response
        }
        response.message="invalid request"
        response.success=false
        return response
    }

    @Throws(Exception::class)
    private fun getAnyTimeZoneToUtcTimeZoneDate(strDate: String,timeZone: String):Date {
        val formatterIST: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
        formatterIST.timeZone = TimeZone.getTimeZone(timeZone)
        val date:Date = formatterIST.parse(strDate)

        val formatterUTC: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        formatterUTC.timeZone = TimeZone.getTimeZone("UTC") // UTC timezone
        return formatterUTC.parse(formatterUTC.format(date))

    }
    @Throws(Exception::class)
    private fun getUtcTimeZoneToAnyTimeZoneDate(date: Date,timeZone: String):String {
        val formatterUTC: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
        formatterUTC.timeZone = TimeZone.getTimeZone("UTC")
        val stdate = formatterUTC.format(date)

        val formatterAny: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
        formatterAny.timeZone = TimeZone.getTimeZone(timeZone) // UTC timezone
        return formatterAny.format(formatterAny.parse(stdate))

    }
}