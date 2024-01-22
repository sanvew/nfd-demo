package xyz.sanvew.location

import com.google.inject.Inject
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import xyz.sanvew.web.Routing

class LocationRoute @Inject constructor(private val locationService: LocationService): Routing {
    override fun route(): Route.() -> Unit = fun Route.() {
        route("/geocoder/search") {
            get("/location") {
                val query = call.parameters["q"] ?: throw RuntimeException("TODO: fix")
                val lang = call.parameters["lang"] ?: "en"
                call.respond(locationService.search(SearchRequest(query, lang)))
            }
            get("/address") {
                val lat = call.parameters["lat"] ?: throw RuntimeException("TODO: fix")
                val lng = call.parameters["lng"] ?: throw RuntimeException("TODO: fix")
                val lang = call.parameters["lang"] ?: "en"
                call.respond(locationService.reverse(ReverseRequest(lat, lng, lang)))
            }
        }
    }
}