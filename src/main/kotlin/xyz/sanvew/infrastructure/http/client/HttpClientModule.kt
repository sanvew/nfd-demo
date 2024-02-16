package xyz.sanvew.infrastructure.http.client

import io.ktor.client.*
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import io.ktor.client.engine.*
import io.ktor.client.plugins.logging.*
import xyz.sanvew.core.search.SearchDataProvider

class HttpClientModule : AbstractModule() {
    override fun configure() {
        bind(SearchDataProvider::class.java).to(SearchDataProviderImpl::class.java).`in`(Singleton::class.java)
    }

    @Provides
    @Singleton
    fun provideHttpClient(engine: HttpClientEngine): HttpClient {
        return HttpClient(engine) {
            expectSuccess = true
            install(Logging)
        }
    }
}