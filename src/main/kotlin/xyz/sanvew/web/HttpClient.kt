package xyz.sanvew.web

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import io.ktor.client.plugins.logging.*

class HttpClientModule: AbstractModule() {
    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(CIO) {
            expectSuccess = true
            install(Logging)
        }
    }
}