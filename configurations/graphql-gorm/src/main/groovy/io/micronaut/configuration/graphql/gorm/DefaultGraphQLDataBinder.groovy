package io.micronaut.configuration.graphql.gorm

import io.micronaut.core.bind.BeanPropertyBinder
import org.grails.gorm.graphql.binding.GraphQLDataBinder

class DefaultGraphQLDataBinder implements GraphQLDataBinder {

    private final BeanPropertyBinder beanPropertyBinder

    DefaultGraphQLDataBinder(BeanPropertyBinder beanPropertyBinder) {
        this.beanPropertyBinder = beanPropertyBinder
    }

    @Override
    void bind(Object object, Map data) {
        beanPropertyBinder.bind(object, data)
    }
}
