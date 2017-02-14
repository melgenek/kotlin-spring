package spring.kotlin.web.util

import org.springframework.core.io.Resource
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono

fun RouterFunction<*>.route(request: ServerRequest, configure: RouterDsl.() -> Unit) =
		RouterDsl().apply(configure).invoke(request)

typealias AA = RequestPredicate.() -> Unit

class RouterDsl {

	val children = mutableListOf<RouterDsl>()
	val routes = mutableListOf<RouterFunction<ServerResponse>>()

	infix fun RequestPredicate.and(other: RequestPredicate): RequestPredicate = this.and(other)

	infix fun RequestPredicate.or(other: RequestPredicate): RequestPredicate = this.or(other)

	operator fun RequestPredicate.not(): RequestPredicate = this.negate()

//	val html: RequestPredicate
//		get() = RequestPredicates.html()

//	operator fun RequestPredicate.invoke(f: RequestPredicate.() -> Unit): RequestPredicate = this.apply(f)

	operator fun RequestPredicate.invoke(f: (ServerRequest) -> Mono<ServerResponse>) {
		routes += RouterFunctions.route(this, HandlerFunction { f(it) })
	}


	fun GET(pattern: String, f: (ServerRequest) -> Mono<ServerResponse>) {
		routes += RouterFunctions.route(RequestPredicates.GET(pattern), HandlerFunction { f(it) })
	}

	fun HEAD(pattern: String, f: (ServerRequest) -> Mono<ServerResponse>) {
		routes += RouterFunctions.route(RequestPredicates.HEAD(pattern), HandlerFunction { f(it) })
	}

	fun POST(pattern: String, f: (ServerRequest) -> Mono<ServerResponse>) {
		routes += RouterFunctions.route(RequestPredicates.POST(pattern), HandlerFunction { f(it) })
	}

	fun PUT(pattern: String, f: (ServerRequest) -> Mono<ServerResponse>) {
		routes += RouterFunctions.route(RequestPredicates.PUT(pattern), HandlerFunction { f(it) })
	}

	fun PATCH(pattern: String, f: (ServerRequest) -> Mono<ServerResponse>) {
		routes += RouterFunctions.route(RequestPredicates.PATCH(pattern), HandlerFunction { f(it) })
	}

	fun DELETE(pattern: String, f: (ServerRequest) -> Mono<ServerResponse>) {
		routes += RouterFunctions.route(RequestPredicates.DELETE(pattern), HandlerFunction { f(it) })
	}

	fun OPTIONS(pattern: String, f: (ServerRequest) -> Mono<ServerResponse>) {
		routes += RouterFunctions.route(RequestPredicates.OPTIONS(pattern), HandlerFunction { f(it) })
	}

	fun accept(mediaType: MediaType, f: (ServerRequest) -> Mono<ServerResponse>) {
		routes += RouterFunctions.route(RequestPredicates.accept(mediaType), HandlerFunction { f(it) })
	}

	fun contentType(mediaType: MediaType, f: (ServerRequest) -> Mono<ServerResponse>) {
		routes += RouterFunctions.route(RequestPredicates.contentType(mediaType), HandlerFunction { f(it) })
	}

	fun headers(headerPredicate: (ServerRequest.Headers) -> Boolean, f: (ServerRequest) -> Mono<ServerResponse>) {
		routes += RouterFunctions.route(RequestPredicates.headers(headerPredicate), HandlerFunction { f(it) })
	}

	fun method(httpMethod: HttpMethod, f: (ServerRequest) -> Mono<ServerResponse>) {
		routes += RouterFunctions.route(RequestPredicates.method(httpMethod), HandlerFunction { f(it) })
	}

	fun path(pattern: String, f: (ServerRequest) -> Mono<ServerResponse>) {
		routes += RouterFunctions.route(RequestPredicates.path(pattern), HandlerFunction { f(it) })
	}

	fun pathExtension(extension: String, f: (ServerRequest) -> Mono<ServerResponse>) {
		routes += RouterFunctions.route(RequestPredicates.pathExtension(extension), HandlerFunction { f(it) })
	}

	fun pathExtension(predicate: (String) -> Boolean, f: (ServerRequest) -> Mono<ServerResponse>) {
		routes += RouterFunctions.route(RequestPredicates.pathExtension(predicate), HandlerFunction { f(it) })
	}

	fun queryParam(name: String, predicate: (String) -> Boolean, f: (ServerRequest) -> Mono<ServerResponse>) {
		routes += RouterFunctions.route(RequestPredicates.queryParam(name, predicate), HandlerFunction { f(it) })
	}

	//	fun json(f: (ServerRequest) -> Mono<ServerResponse>) {
//		dsl += RouterFunctions.route(RequestPredicates.json(), HandlerFunction { f(it) })
//	}
//
	fun html(f: RequestPredicate.() -> Unit) {
		RequestPredicates.html().apply(f)
	}
//
//	fun xml(f: (ServerRequest) -> Mono<ServerResponse>) {
//		dsl += RouterFunctions.route(RequestPredicates.xml(), HandlerFunction { f(it) })
//	}

	fun resources(path: String, location: Resource) {
		routes += RouterFunctions.resources(path, location)
	}

	fun resources(lookupFunction: (ServerRequest) -> Mono<Resource>) {
		routes += RouterFunctions.resources(lookupFunction)
	}

	fun router(): RouterFunction<ServerResponse> {
		println("CREATING ROUTES")
		return routes().reduce(RouterFunction<ServerResponse>::and)
	}

	operator fun invoke(request: ServerRequest): Mono<HandlerFunction<ServerResponse>> {
		return router().route(request)
	}

	private fun routes(): List<RouterFunction<ServerResponse>> {
		val allRoutes = mutableListOf<RouterFunction<ServerResponse>>()
		allRoutes += routes
		for (child in children) {
			allRoutes += child.routes()
		}
		return allRoutes
	}

}
