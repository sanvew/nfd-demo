package xyz.sanvew.infrastructure

import com.google.inject.AbstractModule
import com.google.inject.Singleton
import xyz.sanvew.core.cache.CacheService

private const val DEFAULT_TIMEOUT: Long = 5 * 60 * 1000

class TimeoutCacheService(private val timeout: Long = DEFAULT_TIMEOUT) : CacheService {
    private val cacheMap = mutableMapOf<Any, Pair<Long, Any>>()

    override fun set(key: Any, value: Any) {
        val updateTime = System.currentTimeMillis() + timeout
        cacheMap[key] = Pair(updateTime, value)
    }

    override fun get(key: Any): Any? {
        val expireTimeout = cacheMap[key]?.first
        if (expireTimeout != null && expireTimeout > System.currentTimeMillis()) {
            return cacheMap[key]?.second
        }
        return null
    }
}

class CacheModule : AbstractModule() {
    override fun configure() {
        bind(CacheService::class.java).to(TimeoutCacheService::class.java).`in`(Singleton::class.java)
    }
}
