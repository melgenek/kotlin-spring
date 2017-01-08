package spring.kotlin.web.util

import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.RequestPredicates.POST
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions.route
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
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

typealias Handler = HandlerFunction<ServerResponse>
typealias Route = RouterFunction<ServerResponse>

class Path(val path: String) {

    val children = mutableListOf<Path>()
    val routes = mutableListOf<Route>()

    operator fun String.invoke(init: Path.() -> Unit) {
        path(this, init)
    }

    fun path(path: String, init: Path.() -> Unit) {
        val innerPath = Path(this.path + path)
        innerPath.init()
        children += innerPath
    }

    fun get(f: () -> Handler) {
        routes += route(GET(path), f())
    }

    fun post(f: () -> Handler) {
        routes += route(POST(path), f())
    }

    fun router(): Route {
        return routes().reduce(RouterFunction<*>::and) as Route
    }

    operator fun invoke(request: ServerRequest): Mono<HandlerFunction<ServerResponse>> {
        return router().route(request)
    }

    private fun routes(): List<Route> {
        val allRoutes = mutableListOf<Route>()
        allRoutes += routes
        for (child in children) {
            allRoutes += child.routes()
        }
        return allRoutes
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("$path\n")
        for (child in children) {
            child.toString(sb)
        }
        return sb.toString()
    }

    private fun toString(sb: StringBuilder) {
        sb.append("$path\n")
        for (child in children) {
            child.toString(sb)
        }
    }

}
