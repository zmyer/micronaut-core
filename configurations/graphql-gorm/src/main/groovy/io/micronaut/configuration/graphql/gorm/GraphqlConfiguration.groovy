package io.micronaut.configuration.graphql.gorm

import groovy.transform.CompileStatic
import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.core.util.Toggleable

@CompileStatic
@ConfigurationProperties("grails.gorm.graphql")
class GraphqlConfiguration implements Toggleable {

    boolean enabled = false
    Optional<List<String>> dateFormats = Optional.empty()
    boolean dateFormatLenient = false
    Optional<Map<String, Class>> listArguments = Optional.empty()
    boolean browser = false
}
