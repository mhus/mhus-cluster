package de.mhus.osgi.cache.api;

import javax.cache.Cache;

public interface CacheCreatorApi {

	<K,V> Cache<K, V> createCache(CoreCacheService<K, V> service);

}
