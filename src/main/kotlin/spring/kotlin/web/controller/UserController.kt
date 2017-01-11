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
                GET(findAll())
                "/empty" {
                    GET { req ->
                        ServerResponse.ok().body(fromPublisher(Flux.just(User("empty"))))
                    }
                }
                "/login/{login}"{
                    GET(::findOther)
                }
                "/controller"{
                    GET(this@UserController::findOne)
                }
                "/comp"{
                    GET(Companion::findOtherOne)
                }
                "/java"{
                    GET(personHandler::getPerson)
                }
                "/javaStatic"{
                    GET(PersonHandler::getOtherPerson)
                }
                (GET("/include") and contentType(APPLICATION_JSON)) {
                    include()
                }
                "/**"{
                    (!GET("/exclude")) {
                        findAll()
                    }
                }
            }(request)

    fun findOne(req: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok().body(fromPublisher(Mono.just(User("findOne"))))
    }

    fun include() = HandlerFunction {
        ServerResponse.ok().body(fromPublisher(Flux.just(User("include"))))
    }

    fun findAll() = HandlerFunction {
        ServerResponse.ok().body(fromPublisher(Flux.just(User("first"), User("second"))))
    }

    companion object {
        fun findOtherOne(req: ServerRequest): Mono<ServerResponse> {
            return ServerResponse.ok().body(fromPublisher(Flux.just(User("findOtherOne"))))
        }
    }

}

fun findOther(req: ServerRequest): Mono<ServerResponse> {
    return ServerResponse.ok().body(fromPublisher(Flux.just(User("findOther"))))
}

