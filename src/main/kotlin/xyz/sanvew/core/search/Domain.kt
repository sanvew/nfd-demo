package xyz.sanvew.core.search

// Domain level entities
data class SearchLocation(val lat: Double, val lng: Double, val address: String)
data class SearchAddress(val address: String)

// Application level entities
data class SearchRequest(val query: String, val lang: String)
data class SearchResponse(val locations: List<SearchLocation>)

data class ReverseRequest(val lat: Double, val lng: Double, val lang: String)
data class ReverseResponse(val address: String)
