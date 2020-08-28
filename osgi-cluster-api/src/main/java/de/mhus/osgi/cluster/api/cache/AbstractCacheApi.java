package de.mhus.osgi.cluster.api.cache;

import javax.cache.Cache;
import javax.cache.configuration.MutableConfiguration;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import de.mhus.lib.core.MLog;

public abstract class AbstractCacheApi extends MLog implements CacheApi, CacheCreatorApi {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <K,V> CacheService<K, V> getService(Class<? extends CacheService<K,V>> clazz) {
		BundleContext bc = FrameworkUtil.getBundle(getClass()).getBundleContext();
		String name = clazz.getCanonicalName().toLowerCase();
		try {
			for (ServiceReference<CacheService> ref : bc.getServiceReferences(CacheService.class, null)) {
				if (name.equals(ref.getProperty("object.class")))
					//TODO Cache results
						return bc.getService(ref);
			}
		} catch (InvalidSyntaxException e) {
			log().w(e);
		}
		return null;
	}

	@Override
	public <K, V> Cache<K, V> createCache(CoreCacheService<K, V> service) {

		MutableConfiguration<K,V> configuration = new MutableConfiguration<>();
		
		Class<K> keyType = service.getKeyType();
		Class<V> valueType = service.getValueType();
		configuration.setTypes(keyType, valueType);
		
		service.configure(configuration);

		return createCache(service, configuration);
	}

	/**
	 * Create a cache for the service
	 * 
	 * @param service
	 * @param configuration
	 * @return
	 */
	protected abstract <K, V> Cache<K, V> createCache(CoreCacheService<K, V> service, MutableConfiguration<K, V> configuration);

}
