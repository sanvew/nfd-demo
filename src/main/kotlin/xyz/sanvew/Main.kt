package xyz.sanvew

import com.google.inject.Guice
import io.ktor.server.engine.*
import xyz.sanvew.location.LocationModule
import xyz.sanvew.web.ApplicationModule
import xyz.sanvew.web.HttpClientModule

fun main(args: Array<String>) {
    val modules = listOf(
        HttpClientModule(),
        ApplicationModule(),
        LocationModule(),
    )
    val injector = Guice.createInjector(modules)
    val engine = injector.getInstance(ApplicationEngine::class.java)

    engine.start(wait = true)
}
