package de.mhus.osgi.cluster.api.cache;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CacheConfiguration {
	boolean cluster() default false;

	// boolean writeThrough() default false;

	String accessedExpiry() default "";

	boolean readThrough() default false;
	
}
