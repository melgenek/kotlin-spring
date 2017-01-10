package spring.kotlin.web.controller

import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.BodyInsertersExtension.fromPublisher
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.RequestPredicates.contentType
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import sample.PersonHandler
import spring.kotlin.web.entity.User
import spring.kotlin.web.util.invoke

class UserController : RouterFunction<ServerResponse> {

    val personHandler = PersonHandler()

    override fun route(request: ServerRequest) =
            "/user" {
                "custom" {
                    route(contentType(APPLICATION_JSON)) { createOne() }
                }
                GET { findAll() }
                POST { create() }
                "/{login}"{
                    GET2 { req ->
                        Mono.empty()
                    }
                    GET2(::findOther)
                    GET2(Companion::findOtherOne)
                    GET2(this@UserController::findOne)
                    GET2(personHandler::getPerson)
                    GET2(PersonHandler::getOtherPerson)
                    POST { createOne() }
                }
                "/info/{login}"{

                    POST { createOne() }
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

    companion object {
        fun findOtherOne(req: ServerRequest): Mono<ServerResponse> {
            return Mono.empty()
        }
    }

}

fun findOther(req: ServerRequest): Mono<ServerResponse> {
    return Mono.empty()
}