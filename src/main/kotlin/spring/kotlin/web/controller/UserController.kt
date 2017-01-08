package spring.kotlin.web.controller

import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.BodyInsertersExtension.fromPublisher
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.RequestPredicates.GET
import reactor.core.publisher.Flux
import spring.kotlin.web.entity.User


class UserController : RouterFunction<ServerResponse> {

    override fun route(request: ServerRequest?) = RouterFunctions.route(
            GET("/user/"), findAll()
    ).route(request)

    fun findAll() = HandlerFunction {
        ServerResponse.ok().body(fromPublisher(Flux.just(User("first"), User("second"))))
    }


}