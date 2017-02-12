package spring.kotlin.web.controller

import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.TEXT_HTML
import org.springframework.web.reactive.function.fromPublisher
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import spring.kotlin.web.entity.User
import org.springframework.web.reactive.function.server.RequestPredicates.*

class OtherController : RouterFunction<ServerResponse> {

    override fun route(req: ServerRequest) = route(req) {
        accept(TEXT_HTML).apply {
            (GET("/user/") or GET("/users/")) { findOne(req) }
            GET("/user/{login}", this@OtherController::findOne)
        }
        accept(APPLICATION_JSON).apply {
            (GET("/api/user/") or GET("/api/users/")) { findAll() }

        }
    }

    fun findOne(req: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok().body(fromPublisher(Mono.just(User("findOne"))))
    }

    fun findAll() = ServerResponse.ok().body(fromPublisher(Flux.just(User("first"), User("second"))))

}

fun RouterDsl.GET(f: (ServerRequest) -> Mono<ServerResponse>) {

}