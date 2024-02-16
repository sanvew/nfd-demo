package xyz.sanvew.infrastructure.http.routing

import com.google.inject.Inject
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import xyz.sanvew.core.search.SearchService
import xyz.sanvew.core.search.ReverseRequest
import xyz.sanvew.core.search.SearchRequest

class SearchRouter @Inject constructor(private val searchService: SearchService): Routing {
    override fun route(): Route.() -> Unit = fun Route.() {
        route("/geocoder/search") {
            get("/location") {
                val query = call.parameters["q"] ?: throw ValidationException()
                query.isNullOrBlank() && throw ValidationException()
                val lang = call.parameters["lang"] ?: "en"
                call.respond(searchService.search(SearchRequest(query, lang)))
            }
            get("/address") {
                val lat = call.parameters["lat"] ?: throw ValidationException()
                val lng = call.parameters["lng"] ?: throw ValidationException()
                if (lat.isNullOrBlank() || lng.isNullOrBlank()) throw ValidationException()
                val lang = call.parameters["lang"] ?: "en"
                call.respond(searchService.reverse(ReverseRequest(lat.toDouble(), lng.toDouble(), lang)))
            }
        }
    }
}