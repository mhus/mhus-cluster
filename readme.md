

```
feature:repo-add cellar
install -s mvn:javax.cache/cache-api/1.1.1
install mvn:com.hazelcast/hazelcast-all/3.9.4
feature:install cellar-core
feature:install cellar-hazelcast
# edit etc/hazelcast.xml
nano etc/hazelcast.xml
# remove com.hazelcast:3.9.1 and start com.hazelcast:3.9.4
uninstall com.hazelcast/3.9.1
start com.hazelcast/3.9.4
### restart karaf
install -s mvn:de.mhus.osgi/karaf-cache/7.0.0-SNAPSHOT
 ```

Edit etc/hazelcast.xml and add - to enable caching

```
<cache name="Classroom">
    <key-type class-name="java.lang.String" />
    <value-type class-name="java.lang.Object" />
    <statistics-enabled>false</statistics-enabled>
    <management-enabled>false</management-enabled>
</cache>
```


```
feature:repo-add cellar
feature:install cellar-core
feature:install cellar-hazelcast
feature:install cellar-shell
feature:install cellar-bundle

```