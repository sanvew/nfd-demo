package xyz.sanvew.core.cache

interface CacheService {
    fun set(key: Any, value: Any)
    fun get(key: Any): Any?
}