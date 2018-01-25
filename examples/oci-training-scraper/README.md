This is a scraper of OCI training offerings. Visit `/training` endpoint

To evict the cache visit POST `/training/evict`.

This applications is:

- The use of `@PostConstruct` to create a cache.
- The use of the `@Value` annotation to supply configuration parameters.
- An example of the `@Refresable` Scope. 
- An example of how to trigger a refresh event which results in the destruction 
of every bean annotated with scope `@Refreshable`.  

## Deploy

You will need to declare `CLOUDFOUNDRY_USER`, `CLOUDFOUNDRY_PASSWORD`

./gradlew clean  
./gradlew shadowJar  
./gradlew cf-push  
