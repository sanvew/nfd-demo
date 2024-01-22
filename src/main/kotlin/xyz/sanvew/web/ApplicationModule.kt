package xyz.sanvew.web

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.google.inject.*
import io.ktor.client.plugins.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class ApplicationModule : AbstractModule() {
    @Provides
    @Singleton
    fun provideApplicationEngine(environment: ApplicationEngineEnvironment): ApplicationEngine {
        return embeddedServer(Netty, environment)
    }

    @Provides
    @Singleton
    fun provideJacksonSerializer(): ObjectMapper {
        return ObjectMapper().registerKotlinModule().enable(SerializationFeature.INDENT_OUTPUT)
    }

    @Provides
    @Singleton
    fun provideApplicationEngineEnvironment(
        objectMapper: ObjectMapper, routes: Set<Routing>
    ): ApplicationEngineEnvironment {
        return applicationEngineEnvironment {
            connector {
                port = 8080
                host = "127.0.0.1"
            }
            module {
                routing {
                    for (r in routes) {
                        r.route().invoke(this)
                    }
                }
                install(ContentNegotiation) {
                    register(
                        ContentType.Application.Json,
                        JacksonConverter(objectMapper)
                    )
                }
                install(StatusPages) {
                    exception<Throwable> { call, cause ->
                        call.respond("test")
                    }
                }
            }
        }
    }
}