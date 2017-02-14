package spring.kotlin.web.controller

import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import spring.kotlin.web.util.route

@Controller
class MyRouterController : RouterFunction<ServerResponse> {

	override fun route(req: ServerRequest) = route(req) {
		(RequestPredicates.GET("/foo/") or RequestPredicates.GET("/foos/")) { handle(req) }
		RequestPredicates.accept(MediaType.APPLICATION_JSON).apply {
			PUT("/api/foo/") { handleFromClass(req) }
			DELETE("/api/foo/", ::handle)
		}
		html {
			GET("/page", this@MyRouterController::handleFromClass)
		}

	}

	fun handleFromClass(req: ServerRequest) = ServerResponse.ok().body(BodyInserters.fromObject("handleFromClass"))
}

fun handle(req: ServerRequest) = ServerResponse.ok().body(BodyInserters.fromObject("handle"))

