package spring.kotlin.web.controller

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.web.reactive.function.server.ServerResponse
import spring.kotlin.web.SampleService
import spring.kotlin.web.UserService
import spring.kotlin.web.util.KotlinRouterFunction

//@Component
class AnotherUserController(val userService: UserService) : KotlinRouterFunction({

   val sampleService: SampleService? = null

    GET("/super") { req ->
        println(sampleService)
        ServerResponse.ok().body(fromObject(userService.findUser("id")))
    }

})


