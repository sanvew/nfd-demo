package xyz.sanvew.core.search

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.reset
import org.mockito.kotlin.whenever
import xyz.sanvew.core.cache.CacheService

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SearchServiceImplTest {

    val mockSearchDataProvider = mock(SearchDataProvider::class.java)
    val mockCacheService = mock(CacheService::class.java)

    val underTest: SearchServiceImpl = SearchServiceImpl(mockSearchDataProvider, mockCacheService)

    @AfterEach
    fun mocksReset() {
        reset(mockSearchDataProvider)
        reset(mockCacheService)
    }


    @Test
    fun search_success() {
        val searchRequest = SearchRequest("stub_query", "en")

        val expectedResult = SearchResponse(listOf(SearchLocation(59.93863, 30.31413, "stub_address")))

        runBlocking {
            whenever(mockSearchDataProvider.locationsByQuery(any(), any()))
                .thenReturn(listOf(SearchLocation(59.93863, 30.31413, "stub_address")))
            val result = underTest.search(searchRequest)

            assertFalse(expectedResult.locations.isEmpty())
            assertEquals(expectedResult.locations.size, result.locations.size)
            assertEquals(expectedResult.locations[0], result.locations[0])
        }
    }

    @Test
    fun success_reverse() {
        val reverseRequest = ReverseRequest(59.93863, 30.31413, "en")

        val expectedResult = ReverseResponse("stub_address")

        runBlocking {
            whenever(mockSearchDataProvider.addressByCoordinates(any(), any(), any()))
                .thenReturn(SearchAddress("stub_address"))
            val result = underTest.reverse(reverseRequest)

            assertEquals(expectedResult, result)
        }
    }
}