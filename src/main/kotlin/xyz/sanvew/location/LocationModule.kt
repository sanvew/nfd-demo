package xyz.sanvew.location

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.multibindings.Multibinder
import com.google.inject.multibindings.ProvidesIntoSet
import xyz.sanvew.web.Routing

class LocationModule: AbstractModule() {
    override fun configure() {
        bind(LocationService::class.java).to(LocationServiceImpl::class.java).`in`(Singleton::class.java)
        bind(LocationCache::class.java).to(MapBasedLocationCache::class.java).`in`(Singleton::class.java)
    }

    @ProvidesIntoSet
    fun provideLocationRoute(locationRoute: LocationRoute): Routing {
        return locationRoute
    }
}