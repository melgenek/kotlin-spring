package spring.kotlin.web.util

import org.springframework.core.io.Resource
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.RouterFunctions.route
import reactor.core.publisher.Mono
import java.lang.StringBuilder


operator fun String.invoke(init: Path.() -> Unit): Path {
    return path(this, init)
}

fun path(url: String, init: Path.() -> Unit): Path {
    val path = Path(url)
    path.init()
    return path
}

class Path(val base: String) {

    val children = mutableListOf<Path>()
    val routes = mutableListOf<RouterFunction<ServerResponse>>()

    operator fun String.invoke(init: Path.() -> Unit) {
        path(this, init)
    }

    fun path(path: String, init: Path.() -> Unit) {
        val innerPath = Path(base + path)
        innerPath.init()
        children += innerPath
    }

    fun GET(subPath: String): RequestPredicate {
        return RequestPredicates.GET(base + subPath)
    }

    fun GET(f: (ServerRequest) -> Mono<ServerResponse>) {
        routes += route(RequestPredicates.GET(base), HandlerFunction { req -> f(req) })
    }

    fun GET(f: HandlerFunction<ServerResponse>) {
        routes += route(RequestPredicates.GET(base), f)
    }

    operator fun RequestPredicate.invoke(f: () -> HandlerFunction<ServerResponse>) {
        routes += route(this, f())
    }

    infix fun RequestPredicate.and(other: RequestPredicate): RequestPredicate = this.and(other)

    infix fun RequestPredicate.or(other: RequestPredicate): RequestPredicate = this.and(other)

    operator fun RequestPredicate.not(): RequestPredicate = this.negate()

    fun resources(location: Resource) {
        routes += RouterFunctions.resources(base, location)
    }

    fun router(): RouterFunction<ServerResponse> {
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

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("$base\n")
        for (child in children) {
            child.toString(sb)
        }
        return sb.toString()
    }

    private fun toString(sb: StringBuilder) {
        sb.append("$base\n")
        for (child in children) {
            child.toString(sb)
        }
    }
}
