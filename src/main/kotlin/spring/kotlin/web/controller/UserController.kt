package spring.kotlin.web.controller

import org.springframework.web.reactive.function.BodyInsertersExtension.fromPublisher
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import spring.kotlin.web.entity.User
import spring.kotlin.web.util.invoke

class UserController : RouterFunction<ServerResponse> {

    override fun route(request: ServerRequest) =
            "/user" {
                get { findAll() }
                post { create() }
                "/{login}"{
                    handleGet { findOne(it) }
                    post { createOne() }
                }
                "/info/{login}"{
                    handleGet { req ->
                        ServerResponse.ok().body(fromPublisher(Mono.just(User("First: ${req.pathVariable("login")}"))))
                    }
                    post { createOne() }
                }
            }(request)

    fun findOne(req: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok().body(fromPublisher(Mono.just(User("First: ${req.pathVariable("login")}"))))
    }

    fun createOne() = HandlerFunction { req ->
        ServerResponse.ok().body(fromPublisher(Mono.just(User("Created: ${req.pathVariable("login")}"))))
    }

    fun findAll() = HandlerFunction {
        ServerResponse.ok().body(fromPublisher(Flux.just(User("first"), User("second"))))
    }

    fun create() = HandlerFunction {
        ServerResponse.ok().body(fromPublisher(Flux.just(User("post"), User("successful"))))
    }

}
