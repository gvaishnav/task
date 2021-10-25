package com.task.task.controller

import com.task.task.model.Response
import com.task.task.model.req.BuyBitCoinReq
import com.task.task.model.req.PasswordReq
import com.task.task.model.req.UserBitCoinHistoryReq
import com.task.task.model.req.UserReq
import com.task.task.service.BitCoinChangeRateAlgo
import com.task.task.service.BitCoinHistoryService
import com.task.task.service.UserService
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration
import java.util.function.Function


@RestController
@RequestMapping("api/")
@Slf4j
class Controller {
    @Autowired
    internal lateinit var userService: UserService
    @Autowired
    internal lateinit var bitCoinChangeRateAlgo: BitCoinChangeRateAlgo

    @Autowired
    internal lateinit var bitCoinHistoryService: BitCoinHistoryService

    @CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
    @RequestMapping(method = [RequestMethod.POST], path = ["/saveNewUser"])
    fun saveNewUser(@RequestBody  userReq: UserReq): Mono<Response?>? {
        return Mono.just(userService.saveNewUser(userReq))
    }
    @CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
    @RequestMapping(method = [RequestMethod.POST], path = ["/updateUserPassword"])
    fun updateUserPassword(@RequestBody  passwordReq: PasswordReq): Mono<Response?>? {
        return Mono.just(userService.saveUserPassword(passwordReq))
    }

    @CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
    @RequestMapping(method = [RequestMethod.POST], path = ["/buyBitCoin"])
    fun buyBitCoin(@RequestBody  buyBitCoinReq: BuyBitCoinReq): Mono<Response?>? {
        return Mono.just(userService.buyBitCoin(buyBitCoinReq))
    }

    @CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
    @RequestMapping(method = [RequestMethod.POST], path = ["/getUserBitCoinHistory"])
    fun getUserBitCoinHistory(@RequestBody userBitCoinHistoryReq: UserBitCoinHistoryReq): Mono<Response?>?{
        return Mono.just(userService.getUserBitCoinHistory(userBitCoinHistoryReq))
    }


    @CrossOrigin(allowedHeaders = ["*"])
    @GetMapping(value = ["/getCurrentRateBTC"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getResourceUsage(): Flux<Response>? {
        return Flux.interval(Duration.ofSeconds(30))
            .map(Function { it: Long? ->
                BitCoinChangeRateAlgo.getCurrentBTCRate()
            })

    }
}