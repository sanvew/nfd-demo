package xyz.sanvew

import com.google.inject.Guice
import io.ktor.client.engine.cio.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import xyz.sanvew.core.CoreModule
import xyz.sanvew.infrastructure.InfrastructureModule

private val HTTP_CLIENT_ENGINE = CIO.create()

fun main() {
    val modules = listOf(
        CoreModule(),
        InfrastructureModule(httpClientEngine = HTTP_CLIENT_ENGINE)
    )
    val injector = Guice.createInjector(modules)
    val engineEnvironment = injector.getInstance(ApplicationEngineEnvironment::class.java)

    embeddedServer(Netty, engineEnvironment).start(wait = true)
}
