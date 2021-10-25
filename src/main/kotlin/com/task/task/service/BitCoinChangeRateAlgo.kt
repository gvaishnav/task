package com.task.task.service

import com.task.task.Utils
import com.task.task.model.BitCoinHistory
import com.task.task.model.Response
import com.task.task.repository.BitCoinHistoryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.util.*
@Service
@Component
class BitCoinChangeRateAlgo {
    @Autowired
    internal lateinit var  bitCoinHistoryRepository : BitCoinHistoryRepository
    companion object  {
        val response=Response()
         fun getCurrentBTCRate():Response{
             return response
         }

    }
    @Scheduled(fixedDelay = 1000 * 60, initialDelay = 6000)
    fun autoChangeRate() {
        val pageable: Pageable = PageRequest.of(0, 1, Sort.by("createdAt").descending())
       val pages: Page<BitCoinHistory> = bitCoinHistoryRepository.findAll(pageable)
        if (pages.hasContent()){
            val bitCoinHistoryList: List<BitCoinHistory> = pages.content
            val bitCoinHistory = bitCoinHistoryList[0]
            val rate = bitCoinHistory.rate
            var maxRate: Long=5
            var minRate:Long=1
            rate?.let {
                maxRate = if (rate<1){
                    5
                }else{
                    rate.plus(5)
                }
                minRate = if (rate-5<1){
                    1
                }else{
                    rate.minus(5)
                }
            }
            val  currentRate = (minRate..maxRate).random()
            val currentBitCoinHistory= BitCoinHistory()
            currentBitCoinHistory.rate=currentRate
            currentBitCoinHistory.createdAt= Date()
            currentBitCoinHistory.id=Utils.uniqueId
            val res = bitCoinHistoryRepository.saveAndFlush(currentBitCoinHistory)
            response.data=res
            response.success=true
            response.message="Success"
        }else{
            val currentBitCoinHistory= BitCoinHistory()
            currentBitCoinHistory.rate=1
            currentBitCoinHistory.createdAt= Date()
            currentBitCoinHistory.id=Utils.uniqueId
            val res =bitCoinHistoryRepository.saveAndFlush(currentBitCoinHistory)
            response.data=res
            response.success=true
            response.message="Success"
        }
    }

}