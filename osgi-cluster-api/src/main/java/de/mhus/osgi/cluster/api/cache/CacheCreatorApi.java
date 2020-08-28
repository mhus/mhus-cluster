package de.mhus.osgi.cluster.api.cache;

import javax.cache.Cache;

public interface CacheCreatorApi {

	<K,V> Cache<K, V> createCache(CoreCacheService<K, V> service);

}
