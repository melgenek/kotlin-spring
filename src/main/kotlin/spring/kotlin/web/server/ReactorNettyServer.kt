package spring.kotlin.web.server


import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.ListableBeanFactoryExtension.getBeansOfType
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter
import org.springframework.web.reactive.function.server.HandlerStrategies
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.server.adapter.WebHttpHandlerBuilder
import reactor.ipc.netty.NettyContext
import reactor.ipc.netty.http.server.HttpServer
import java.util.concurrent.atomic.AtomicReference

class ReactorNettyServer(hostname: String, port: Int) : Server, ApplicationContextAware, InitializingBean {

    override val isRunning: Boolean
        get() {
            val context = nettyContext.get()
            return context != null && context.channel().isActive
        }

    private val server = HttpServer.create(hostname, port)
    private val nettyContext = AtomicReference<NettyContext>()
    lateinit private var appContext: ApplicationContext
    lateinit private var reactorHandler: ReactorHttpHandlerAdapter

    override fun setApplicationContext(context: ApplicationContext) {
        appContext = context
    }

    override fun afterPropertiesSet() {
        val controllers = appContext.getBeansOfType(RouterFunction::class).values
        val router = controllers.reduce(RouterFunction<*>::and)
        val strategies = HandlerStrategies.builder().build()
        val webHandler = RouterFunctions.toHttpHandler(router, strategies)
        println(strategies)
        println(webHandler)
        val httpHandler = WebHttpHandlerBuilder.webHandler(webHandler).build()
        reactorHandler = ReactorHttpHandlerAdapter(httpHandler)
    }

    override fun start() {
        if (!isRunning) {
            if (nettyContext.get() == null) {
                nettyContext.set(server.newHandler(reactorHandler)
                        .doOnNext { println("Reactor Netty server started on ${it.address()}") }
                        .block())
            }
        }
    }

    override fun stop() {
        if (isRunning) {
            val context = nettyContext.getAndSet(null)
            context.dispose()
            context.onClose()
        }
    }

}