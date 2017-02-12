package spring.kotlin.web.controller

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.web.reactive.function.server.ServerResponse
import spring.kotlin.web.util.KotlinRouterFunction

@Component
class AnotherUserController : KotlinRouterFunction({

    GET("/super") { req ->
        ServerResponse.ok().body(fromObject("super!!!"))
    }

})
