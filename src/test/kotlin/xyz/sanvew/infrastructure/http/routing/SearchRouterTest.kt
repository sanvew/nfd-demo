package xyz.sanvew.infrastructure.http.routing

import io.ktor.client.engine.mock.*
import io.ktor.http.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import xyz.sanvew.infrastructure.InfrastructureTestBase

private const val LOCATION_PATH = "/geocoder/search/location"
private const val ADDRESS_PATH = "/geocoder/search/address"

class SearchRouterTest : InfrastructureTestBase() {

    @Test
    fun locationGET_success() {
        val query = "bakery"
        val lang = "en"
        val requestUri = "$LOCATION_PATH?q=$query&lang=$lang"
        request(HttpMethod.Get, requestUri, mockHttpClientRequestHandler = {
            request ->
                respond(
                    content =
                        """[
                        |       {
                        |           "lat": "1.1",
                        |           "lon": "1.1",
                        |           "display_name": "stub_display_name"
                        |       }
                        |]""".trimMargin(),
                    status = HttpStatusCode.OK
                )
        }).apply {
            assertEquals(HttpStatusCode.OK, status())
        }
    }

    @Test
    fun locationGET_failed_blankParam() {
        val query = ""
        val lang = "en"
        val requestUri = "$LOCATION_PATH?q=$query&lang=$lang"
        request(HttpMethod.Get, requestUri).apply {
            assertEquals(HttpStatusCode.BadRequest, status())
        }
    }

    @Test
    fun addressGET_success() {
        val successLat = 1
        val successLng = 1
        val lang = "en"
        val requestUri = "$ADDRESS_PATH?lat=$successLat&lng=$successLng&lang=$lang"
        request(HttpMethod.Get, requestUri, mockHttpClientRequestHandler = {
            request ->
                respond(
                    content = """{"display_name": "stub_location"}""",
                    status = HttpStatusCode.OK
                )
        }).apply {
            assertEquals(HttpStatusCode.OK, status())
        }
    }

    @Test
    fun addressGET_failed_blankParam() {
        val successLat = ""
        val successLng = ""
        val lang = "en"
        val requestUri = "$ADDRESS_PATH?lat=$successLat&lng=$successLng&lang=$lang"
        request(HttpMethod.Get, requestUri).apply {
            assertEquals(HttpStatusCode.BadRequest, status())
        }
    }

    @Test
    fun addressGET_failed_NotFound() {
        val successLat = -1000
        val successLng = -1000
        val lang = "en"
        val requestUri = "$ADDRESS_PATH?lat=$successLat&lng=$successLng&lang=$lang"
        request(HttpMethod.Get, requestUri, mockHttpClientRequestHandler = {
                request ->
            respond(
                content = """{"error": "stub_error_message"}""",
                status = HttpStatusCode.OK
            )
        }).apply {
            assertEquals(HttpStatusCode.NotFound, status())
        }
    }
}