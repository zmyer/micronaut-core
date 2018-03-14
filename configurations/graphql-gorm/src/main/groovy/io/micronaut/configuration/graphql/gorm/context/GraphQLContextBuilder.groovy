package io.micronaut.configuration.graphql.gorm.context

import io.micronaut.http.HttpRequest

interface GraphQLContextBuilder {

    Object buildContext(HttpRequest request)
}
