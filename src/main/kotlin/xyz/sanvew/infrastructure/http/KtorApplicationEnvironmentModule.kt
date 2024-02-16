package xyz.sanvew.infrastructure.http

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import xyz.sanvew.core.CoreException
import xyz.sanvew.core.NothingFoundException
import xyz.sanvew.infrastructure.http.routing.ApiException
import xyz.sanvew.infrastructure.http.routing.Routing

class KtorApplicationEnvironmentModule : AbstractModule() {

    @Provides
    @Singleton
    fun provideApplicationEngineEnvironment(
        objectMapper: ObjectMapper, routes: Set<Routing>
    ): ApplicationEngineEnvironment {
        return applicationEngineEnvironment {
            connector {
                port = 8080
                host = "0.0.0.0"
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
                    exception<ApiException> { call, cause ->
                        call.respond(cause.httpStatus)
                    }

                    exception<CoreException> { call, cause ->
                        when (cause) {
                            is NothingFoundException -> call.respond(HttpStatusCode.NotFound)
                            else -> call.respond(HttpStatusCode.InternalServerError)
                        }
                    }

                    exception<Exception> { call, _ ->
                        call.respond(HttpStatusCode.InternalServerError)
                    }
                }
            }
        }
    }
}