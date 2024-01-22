package xyz.sanvew.web

import io.ktor.server.routing.*

interface Routing {
    fun route(): Route.() -> Unit
}
