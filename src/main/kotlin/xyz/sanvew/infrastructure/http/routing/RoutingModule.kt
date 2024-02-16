package xyz.sanvew.infrastructure.http.routing

import com.google.inject.AbstractModule
import com.google.inject.multibindings.Multibinder

class RoutingModule : AbstractModule() {
    override fun configure() {
        val routingsBinder = Multibinder.newSetBinder(binder(), Routing::class.java)
        routingsBinder.addBinding().to(SearchRouter::class.java)
    }
}