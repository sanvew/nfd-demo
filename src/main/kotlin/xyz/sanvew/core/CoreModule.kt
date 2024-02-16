package xyz.sanvew.core

import com.google.inject.AbstractModule
import xyz.sanvew.core.search.SearchModule

class CoreModule: AbstractModule() {
    override fun configure() {
        install(SearchModule())
    }
}