package spring.kotlin.web.server

interface Server {
    fun start()
    fun stop()
    val isRunning: Boolean
}