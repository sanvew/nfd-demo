package xyz.sanvew.core.search

import com.google.inject.Inject
import xyz.sanvew.core.cache.CacheService

/**
 * LocationService is an application/domain layer
 */
interface SearchService {
    suspend fun search(dto: SearchRequest): SearchResponse
    suspend fun reverse(dto: ReverseRequest): ReverseResponse
}

class SearchServiceImpl @Inject constructor(
    private val searchDataProvider: SearchDataProvider, private val cacheService: CacheService
): SearchService {
    override suspend fun search(dto: SearchRequest): SearchResponse {
        val resp = cacheService.get(dto) as SearchResponse?
        return resp ?: SearchResponse(searchDataProvider.locationsByQuery(dto.query, dto.lang)).apply {
            cacheService.set(dto, this)
        }
    }

    override suspend fun reverse(dto: ReverseRequest): ReverseResponse {
        val resp = cacheService.get(dto) as ReverseResponse?
        return resp ?: ReverseResponse(searchDataProvider.addressByCoordinates(dto.lat, dto.lng, dto.lang).address).apply {
            cacheService.set(dto, this)
        }
    }
}
