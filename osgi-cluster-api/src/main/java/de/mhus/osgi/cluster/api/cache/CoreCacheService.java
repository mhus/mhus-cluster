package de.mhus.osgi.cluster.api.cache;

import java.util.HashMap;
import java.util.Map;

import javax.cache.Cache;
import javax.cache.configuration.Factory;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.AccessedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.integration.CacheLoader;
import javax.cache.integration.CacheLoaderException;

import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.cfg.CfgBoolean;
import de.mhus.lib.errors.MRuntimeException;

public abstract class CoreCacheService<K, V> extends MLog implements CacheService<K, V> {

	private static CfgBoolean CFG_STATISTICS_ENABLED = new CfgBoolean(CacheApi.class, "statisticsEnabled", false);
	private CacheCreatorApi api;
	private Cache<K, V> cache;

	@Reference(unbind="unbindCacheApi", policy=ReferencePolicy.DYNAMIC, cardinality=ReferenceCardinality.MANDATORY)
	public void setCacheApi(CacheCreatorApi api) {
		this.api = api;
	}
	
	public void unbindCacheApi() {
		api = null;
		cache = null;
	}
	
	@Override
	public Cache<K, V> getCache() {
		if (cache == null) {
			cache = api.createCache(this);
		}
		return cache;
	}

	@SuppressWarnings("unchecked")
	public Class<V> getValueType() {
		String valTypeStr = MSystem.getTemplateCanonicalName(getClass(), 1);
		return (Class<V>) getType(valTypeStr);
	}

	@SuppressWarnings("unchecked")
	public Class<K> getKeyType() {
		String keyTypeStr = MSystem.getTemplateCanonicalName(getClass(), 0);
		return (Class<K>) getType(keyTypeStr);
	}

	public Class<?> getType(String typeStr) {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		try {
			return cl.loadClass(typeStr);
		} catch (ClassNotFoundException e) {
			throw new MRuntimeException("type not found",typeStr,e);
		}
	}

	public String getCacheName() {
		return getClass().getCanonicalName().toLowerCase();
	}
	
	public boolean isClusterCache() {
		CacheConfiguration def = getClass().getAnnotation(CacheConfiguration.class);
		if (def == null) return false;
		return def.cluster();
	}

	@Override
    public void configure(MutableConfiguration<K,V> configuration) {
		CacheConfiguration def = getClass().getAnnotation(CacheConfiguration.class);
		if (def == null) return;
//		if (def.writeThrough()) {
//			configuration.setWriteThrough(def.writeThrough());
//			configuration.setCacheWriterFactory(new Factory<CacheWriter<K, V>>() {
//				private static final long serialVersionUID = 1L;
//
//				@Override
//				public CacheWriter<K, V> create() {
//					return CoreCacheService.this.createWriter();
//				}
//				
//			});
//		}
		if (def.readThrough()) {
			configuration.setReadThrough(def.readThrough());
			configuration.setCacheLoaderFactory(new Factory<CacheLoader<K, V>>() {
				private static final long serialVersionUID = 1L;

				@Override
				public CacheLoader<K, V> create() {
					return CoreCacheService.this.createLoader();
				}
				
			});
		}
		if (MString.isSet(def.accessedExpiry())) {
			Duration duration = parseDuration(def.accessedExpiry());
			configuration.setExpiryPolicyFactory(AccessedExpiryPolicy.factoryOf(duration));
		}
		if (CFG_STATISTICS_ENABLED.value())
			configuration.setStatisticsEnabled(true);
	}

	protected Duration parseDuration(String str) {
		//TODO
		return null;
	}

	public CacheLoader<K, V> createLoader() {
		return new CacheLoader<K, V>() {

			@Override
			public V load(K key) throws CacheLoaderException {
				return CoreCacheService.this.load(key);
			}

			@Override
			public Map<K, V> loadAll(Iterable<? extends K> keys) throws CacheLoaderException {
				return CoreCacheService.this.loadAll(keys);
			}
		};
	}
	
	public Map<K, V> loadAll(Iterable<? extends K> keys) throws CacheLoaderException {
		Map<K,V> ret = new HashMap<>();
		for (K key : keys)
			ret.put(key, load(key));
		return ret;
	}

	public V load(K key) throws CacheLoaderException {
		return null;
	}

//	public CacheLoader<K, V> createWriter() {
//		return new CacheWriter<K,V>() {
//
//			@Override
//			public void write(Entry<? extends K, ? extends V> entry) throws CacheWriterException {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void writeAll(Collection<Entry<? extends K, ? extends V>> entries) throws CacheWriterException {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void delete(Object key) throws CacheWriterException {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void deleteAll(Collection<?> keys) throws CacheWriterException {
//				// TODO Auto-generated method stub
//				
//			}
//		};
//	}

}
