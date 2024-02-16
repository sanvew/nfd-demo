package xyz.sanvew.infrastructure

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.engine.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.mockito.Mockito.mock
import xyz.sanvew.core.CoreModule
import xyz.sanvew.core.cache.CacheService
import xyz.sanvew.infrastructure.http.HttpModule
import java.util.concurrent.TimeUnit

internal typealias MockHttpClientRequestHandler = MockRequestHandleScope.(HttpRequestData) -> HttpResponseData

internal val DEFAULT_HTTP_CLIENT_REQUEST_HANDLER: MockHttpClientRequestHandler = {
    _ -> respondError(HttpStatusCode.Forbidden)
}

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class InfrastructureTestBase {
    private lateinit var appEngine: TestApplicationEngine
    private lateinit var httpClient: MockEngine
    private var mockHttpClientRequestHandler = DEFAULT_HTTP_CLIENT_REQUEST_HANDLER

    private class MockInfrastructureModule: AbstractModule() {
        override fun configure() {
            bind(CacheService::class.java).toInstance(mock(CacheService::class.java))
        }
    }

    protected lateinit var injector: Injector

    @BeforeAll
    protected fun setupTestServer(): Unit {
        httpClient = mockHttpClientEngine()
        val modules = listOf(
            CoreModule(),
            // infrastructureModules
            HttpModule(httpClient),
            MockInfrastructureModule(),
            JsonModule()
        )

        injector = Guice.createInjector(modules)
        val engineEnvironment = injector.getInstance(ApplicationEngineEnvironment::class.java)
        appEngine = TestEngine.create(engineEnvironment) {}
        appEngine.start()
    }

    @AfterAll
    protected fun stopTestServer() {
        appEngine.stop(0L, 0L, TimeUnit.MILLISECONDS)
    }

    @AfterEach
    protected fun resetMockHttpClientRequestHandler() {
        mockHttpClientRequestHandler = DEFAULT_HTTP_CLIENT_REQUEST_HANDLER
    }

    fun request(
        httpMethod: HttpMethod, uri: String,
        requestSetup: TestApplicationRequest.() -> Unit = {},
        mockHttpClientRequestHandler: MockHttpClientRequestHandler? = null
    ): TestApplicationResponse {
        if (mockHttpClientRequestHandler != null) {
            this.mockHttpClientRequestHandler = mockHttpClientRequestHandler
        }
        val appCall = appEngine.handleRequest(httpMethod, uri) {requestSetup()}
        return appCall.response
    }

    private fun mockHttpClientEngine(): MockEngine {
        return MockEngine { request -> mockHttpClientRequestHandler(request) }
    }
}

