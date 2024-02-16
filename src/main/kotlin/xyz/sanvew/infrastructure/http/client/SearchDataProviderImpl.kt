package xyz.sanvew.infrastructure.http.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.contains
import com.google.inject.Inject
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import xyz.sanvew.core.NothingFoundException
import xyz.sanvew.core.search.SearchDataProvider
import xyz.sanvew.core.search.SearchAddress
import xyz.sanvew.core.search.SearchLocation

private const val NOMINATIM_URL = "https://nominatim.openstreetmap.org"
private const val DEFAULT_RESULT_LIMIT = 10

class SearchDataProviderImpl @Inject constructor(
    private val httpClient: HttpClient, private val objectMapper: ObjectMapper
): SearchDataProvider {
    override suspend fun locationsByQuery(query: String, lang: String): List<SearchLocation> {
        val resp = httpClient.get(NOMINATIM_URL) {
            url {
                appendEncodedPathSegments("search")
                parameters.append("q", query)
                parameters.append("accept-language", lang)
                parameters.append("format", "jsonv2")
                parameters.append("limit", DEFAULT_RESULT_LIMIT.toString())
            }
        }

        return objectMapper.readTree(resp.bodyAsText()).map { location ->
            SearchLocation(
                location.get("lat").asDouble(),
                location.get("lon").asDouble(),
                location.get("display_name").asText()
            )
        }
    }

    override suspend fun addressByCoordinates(lat: Double, lng: Double, lang: String): SearchAddress {
        val resp = httpClient.get(NOMINATIM_URL) {
            url {
                appendEncodedPathSegments("reverse")
                parameters.append("lat", lat.toString())
                parameters.append("lon", lng.toString())
                parameters.append("accept-language", lang)
                parameters.append("format", "jsonv2")
            }
        }

        val body = objectMapper.readTree(resp.bodyAsText())
        if (body.contains("error")) {
            throw NothingFoundException()
        }

        return SearchAddress(body.get("display_name").asText())
    }
}