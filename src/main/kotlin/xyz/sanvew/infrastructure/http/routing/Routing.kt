package xyz.sanvew.infrastructure.http.routing

import io.ktor.server.routing.*

interface Routing {
    fun route(): Route.() -> Unit
}
