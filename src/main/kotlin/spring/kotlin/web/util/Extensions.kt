package spring.kotlin.web.util

import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono

abstract class KotlinRouterFunction(val routes: RouterDsl.() -> Unit) : RouterFunction<ServerResponse> {

    companion object {
        fun invoke(f: () -> Unit) {

        }
    }

    override fun route(request: ServerRequest): Mono<HandlerFunction<ServerResponse>> = route(request, routes)

}