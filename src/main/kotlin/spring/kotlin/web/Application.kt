package spring.kotlin.web


import org.apache.log4j.Level
import org.apache.log4j.Logger
import org.springframework.beans.factory.BeanFactoryExtension.getBean
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.support.GenericApplicationContextExtension.registerBean
import spring.kotlin.web.controller.UserController
import spring.kotlin.web.server.ReactorNettyServer
import java.util.concurrent.CompletableFuture
import java.util.function.Supplier
import spring.kotlin.web.server.Server
import spring.kotlin.web.util.addPropertySource

class Application(val port: Int? = null, val hostname: String = "0.0.0.0") {

    var context: AnnotationConfigApplicationContext? = null
    var server: Server? = null

    fun start() {
        Logger.getLogger("reactor").level = Level.OFF
        val context = AnnotationConfigApplicationContext()
        val env = context.environment
        env.addPropertySource("application.properties")

        context.registerBean(Supplier { ReactorNettyServer(hostname, port ?: env.getProperty("server.port").toInt()) })
        context.registerBean(UserController::class)

        context.refresh()

        val server = context.getBean(Server::class)
        server.start()

        this.context = context
        this.server = server
    }

    fun await() {
        val stop = CompletableFuture<Void>()
        Runtime.getRuntime().addShutdownHook(Thread {
            stop()
            stop.complete(null)
        })
        stop.get()
    }

    fun stop() {
        server?.stop()
        context?.destroy()
    }

}

fun main(args: Array<String>) {
    val application = Application()
    application.start()
    application.await()
}