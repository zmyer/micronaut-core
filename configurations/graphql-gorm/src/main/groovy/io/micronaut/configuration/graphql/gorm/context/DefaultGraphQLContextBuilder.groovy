package io.micronaut.configuration.graphql.gorm.context

import groovy.transform.CompileStatic
import io.micronaut.http.HttpRequest

@CompileStatic
class DefaultGraphQLContextBuilder implements GraphQLContextBuilder {

    @Override
    Object buildContext(HttpRequest request) {
        [locale: request.locale.orElse(Locale.default)]
    }
}
