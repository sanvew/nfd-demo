package xyz.sanvew.core.search

interface SearchDataProvider {
    suspend fun locationsByQuery(query: String, lang: String): List<SearchLocation>
    suspend fun addressByCoordinates(lat: Double, lng: Double, lang: String): SearchAddress
}

