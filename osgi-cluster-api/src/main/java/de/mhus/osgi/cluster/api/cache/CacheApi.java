package de.mhus.osgi.cluster.api.cache;

import javax.cache.Cache;

public interface CacheApi {
    
	<K,V> CacheService<K, V> getService(Class<? extends CacheService<K,V>> clazz);
	
	default <K,V> Cache<K, V> getCache(Class<? extends CacheService<K,V>> clazz) {
		return getService(clazz)
				.getCache();
	}
	
}
