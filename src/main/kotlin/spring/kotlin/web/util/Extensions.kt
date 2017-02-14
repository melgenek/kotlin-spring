package spring.kotlin.web.util

import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

abstract class KotlinRouterFunction(val routes: RouterDsl.() -> Unit) : RouterFunction<ServerResponse> {

	companion object {
		fun invoke(f: () -> Unit) {

		}
	}

	override fun route(request: ServerRequest): Mono<HandlerFunction<ServerResponse>> = route(request, routes)

}

abstract class LazyRouterFunction : RouterFunction<ServerResponse> {

	val routerFunction: RouterFunction<ServerResponse> by lazy {
		RouterDsl().apply(routes).router()
	}

	abstract val routes: RouterDsl.() -> Unit

	override fun route(request: ServerRequest): Mono<HandlerFunction<ServerResponse>> {
		return routerFunction.route(request)
	}

}