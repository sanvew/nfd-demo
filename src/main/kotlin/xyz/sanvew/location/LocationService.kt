package xyz.sanvew.location

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.Inject
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

interface LocationService {
    suspend fun search(dto: SearchRequest): SearchResponse

    suspend fun reverse(dto: ReverseRequest): ReverseResponse
}

interface LocationCache {
    fun addSearch(search: SearchRequest, searchLocations: List<SearchLocation>)
    fun bySearch(search: SearchRequest): List<SearchLocation>?

    fun addReverse(reverse: ReverseRequest, address: String)
    fun byReverse(reverse: ReverseRequest): String?
}

class LocationServiceImpl @Inject constructor(
    private val locationCache: LocationCache, private val objectMapper: ObjectMapper, private val httpClient: HttpClient
): LocationService {
    companion object {
        private const val NOMINATIM_URL = "https://nominatim.openstreetmap.org"
        private const val DEFAULT_RESULT_LIMIT = 10
    }

    override suspend fun search(dto: SearchRequest): SearchResponse {
        val locations =
            locationCache.bySearch(dto) ?: fetchLocations(dto).also { locationCache.addSearch(dto, it) }
        return SearchResponse(locations)
    }

    override suspend fun reverse(dto: ReverseRequest): ReverseResponse {
        val address =
            locationCache.byReverse(dto) ?: fetchAddress(dto).also { locationCache.addReverse(dto, it) }
        return ReverseResponse(address)
    }

    private suspend fun fetchAddress(dto: ReverseRequest): String {
        val resp = httpClient.get(NOMINATIM_URL) {
            url {
                appendEncodedPathSegments("reverse")
                parameters.append("lat", dto.lat)
                parameters.append("lon", dto.lng)
                parameters.append("accept-language", dto.lang)
                parameters.append("format", "jsonv2")
            }
        }
        val jsonResp = objectMapper.readTree(resp.bodyAsText())
        return jsonResp.get("display_name").asText()
    }

    private suspend fun fetchLocations(dto: SearchRequest): List<SearchLocation> {
        val resp = httpClient.get(NOMINATIM_URL) {
            url {
                appendEncodedPathSegments("search")
                parameters.append("q", dto.address)
                parameters.append("accept-language", dto.lang)
                parameters.append("format", "jsonv2")
                parameters.append("limit", DEFAULT_RESULT_LIMIT.toString())
            }
        }
        return objectMapper.readTree(resp.bodyAsText()).map {
                location -> SearchLocation(location.get("lat").asDouble(), location.get("lon").asDouble(), location.get("display_name").asText())
        }
    }
}

class MapBasedLocationCache: LocationCache {
    private val searchCache: MutableMap<SearchRequest, List<SearchLocation>> = mutableMapOf()
    private val reverseCache: MutableMap<ReverseRequest, String> = mutableMapOf()

    override fun addSearch(search: SearchRequest, searchLocations: List<SearchLocation>) {
        searchCache += Pair(search, searchLocations)
    }

    override fun bySearch(search: SearchRequest): List<SearchLocation>? = searchCache[search]

    override fun addReverse(reverse: ReverseRequest, address: String) {
        reverseCache += Pair(reverse, address)
    }

    override fun byReverse(reverse: ReverseRequest): String? = reverseCache[reverse]
}

