package xyz.sanvew.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton

class JsonModule : AbstractModule() {
    @Provides
    @Singleton
    fun provideJacksonSerializer(): ObjectMapper {
        return ObjectMapper().registerKotlinModule().enable(SerializationFeature.INDENT_OUTPUT)
    }
}