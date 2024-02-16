package xyz.sanvew.infrastructure.http

import com.google.inject.AbstractModule
import io.ktor.client.engine.*
import xyz.sanvew.infrastructure.http.client.HttpClientModule
import xyz.sanvew.infrastructure.http.routing.RoutingModule

class HttpModule(private val httpClientEngine: HttpClientEngine) : AbstractModule() {
    override fun configure() {
        bind(HttpClientEngine::class.java).toInstance(httpClientEngine)
        install(HttpClientModule())
        install(RoutingModule())
        install(KtorApplicationEnvironmentModule())
    }
}