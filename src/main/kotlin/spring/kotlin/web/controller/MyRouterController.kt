package spring.kotlin.web.controller

import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.RequestPredicates.*
import reactor.core.publisher.Mono
import spring.kotlin.web.util.LazyRouterFunction
import spring.kotlin.web.util.RouterDsl
import spring.kotlin.web.util.route

@Controller
class MyRouterController : RouterFunction<ServerResponse> {

	override fun route(req: ServerRequest): Mono<HandlerFunction<ServerResponse>> {
		println("ROUTING")
		return route(req) {
			(RequestPredicates.GET("/foo/") or RequestPredicates.GET("/foos/")) { handle(req) }
			RequestPredicates.accept(MediaType.APPLICATION_JSON).apply {
				PUT("/api/foo/") { handleFromClass(req) }
				DELETE("/api/foo/", ::handle)
			}
			html().apply {
				GET("/page", this@MyRouterController::handleFromClass)
			}

		}
	}

	fun handleFromClass(req: ServerRequest) = ServerResponse.ok().body(BodyInserters.fromObject("handleFromClass"))
}

fun handle(req: ServerRequest) = ServerResponse.ok().body(BodyInserters.fromObject("handle"))

@Controller
class LazyController : LazyRouterFunction() {
	override val routes: RouterDsl.() -> Unit = {
		(GET("/foo/") or GET("/foos/")) { req -> handle(req) }
		accept(MediaType.APPLICATION_JSON).apply {
			DELETE("/api/foo/", this@LazyController::handle)
		}
	}

	fun handle(req: ServerRequest) = ServerResponse.ok().body(BodyInserters.fromObject("some text"))
}


