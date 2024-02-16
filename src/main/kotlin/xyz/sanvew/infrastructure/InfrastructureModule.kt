package xyz.sanvew.infrastructure

import com.google.inject.AbstractModule
import io.ktor.client.engine.*
import xyz.sanvew.infrastructure.http.HttpModule

class InfrastructureModule(private val httpClientEngine: HttpClientEngine) : AbstractModule() {
    override fun configure() {
        install(CacheModule())
        install(HttpModule(httpClientEngine))
        install(JsonModule())
    }
}