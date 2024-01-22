package xyz.sanvew.location

data class SearchRequest(val address: String, val lang: String)
data class SearchLocation(val lat: Double, val lng: Double, val address: String)
data class SearchResponse(val locations: List<SearchLocation>)

data class ReverseRequest(val lat: String, val lng: String, val lang: String)
data class ReverseResponse(val address: String)
