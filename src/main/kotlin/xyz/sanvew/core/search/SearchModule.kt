package xyz.sanvew.core.search

import com.google.inject.AbstractModule
import com.google.inject.Singleton

class SearchModule : AbstractModule() {
    override fun configure() {
        bind(SearchService::class.java).to(SearchServiceImpl::class.java).`in`(Singleton::class.java)
    }
}