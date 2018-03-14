package io.micronaut.configuration.graphql.gorm

import graphql.GraphQL
import graphql.schema.GraphQLSchema
import io.micronaut.configuration.graphql.gorm.context.DefaultGraphQLContextBuilder
import io.micronaut.configuration.graphql.gorm.context.GraphQLContextBuilder
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Requires
import io.micronaut.core.bind.BeanPropertyBinder
import org.grails.datastore.mapping.model.MappingContext
import org.grails.gorm.graphql.GraphQLServiceManager
import org.grails.gorm.graphql.Schema
import org.grails.gorm.graphql.binding.GraphQLDataBinder
import org.grails.gorm.graphql.binding.manager.DefaultGraphQLDataBinderManager
import org.grails.gorm.graphql.binding.manager.GraphQLDataBinderManager
import org.grails.gorm.graphql.entity.GraphQLEntityNamingConvention
import org.grails.gorm.graphql.entity.property.manager.DefaultGraphQLDomainPropertyManager
import org.grails.gorm.graphql.entity.property.manager.GraphQLDomainPropertyManager
import org.grails.gorm.graphql.fetcher.manager.DefaultGraphQLDataFetcherManager
import org.grails.gorm.graphql.fetcher.manager.GraphQLDataFetcherManager
import org.grails.gorm.graphql.interceptor.manager.DefaultGraphQLInterceptorManager
import org.grails.gorm.graphql.interceptor.manager.GraphQLInterceptorManager
import org.grails.gorm.graphql.response.delete.DefaultGraphQLDeleteResponseHandler
import org.grails.gorm.graphql.response.delete.GraphQLDeleteResponseHandler
import org.grails.gorm.graphql.response.errors.DefaultGraphQLErrorsResponseHandler
import org.grails.gorm.graphql.response.errors.GraphQLErrorsResponseHandler
import org.grails.gorm.graphql.response.pagination.DefaultGraphQLPaginationResponseHandler
import org.grails.gorm.graphql.response.pagination.GraphQLPaginationResponseHandler
import org.grails.gorm.graphql.types.DefaultGraphQLTypeManager
import org.grails.gorm.graphql.types.GraphQLTypeManager
import org.springframework.context.MessageSource
import org.springframework.context.support.StaticMessageSource

import javax.annotation.Nullable
import javax.inject.Singleton

@Factory
@Requires(property = "grails.gorm.graphql.enabled")
class GraphQLFactory {

    @Singleton
    GraphQLContextBuilder contextBuilder() {
        new DefaultGraphQLContextBuilder()
    }

    @Singleton
    GraphQLEntityNamingConvention namingConvention() {
        new GraphQLEntityNamingConvention()
    }

    @Singleton
    GraphQLDomainPropertyManager propertyManager() {
        new DefaultGraphQLDomainPropertyManager()
    }

    @Singleton
    GraphQLPaginationResponseHandler paginationResponseHandler() {
        new DefaultGraphQLPaginationResponseHandler()
    }

    @Singleton
    GraphQLDataFetcherManager dataFetcherManager() {
        new DefaultGraphQLDataFetcherManager()
    }

    @Singleton
    GraphQLDeleteResponseHandler deleteResponseHandler() {
        new DefaultGraphQLDeleteResponseHandler()
    }

    @Singleton
    GraphQLInterceptorManager interceptorManager() {
        new DefaultGraphQLInterceptorManager()
    }

    @Singleton
    GraphQLServiceManager serviceManager() {
        new GraphQLServiceManager()
    }

    @Singleton
    GraphQLTypeManager typeManager(GraphQLEntityNamingConvention namingConvention,
                                   GraphQLErrorsResponseHandler errorsResponseHandler,
                                   GraphQLDomainPropertyManager domainPropertyManager,
                                   GraphQLPaginationResponseHandler paginationResponseHandler) {
        new DefaultGraphQLTypeManager(namingConvention, errorsResponseHandler, domainPropertyManager, paginationResponseHandler)
    }

    @Singleton
    GraphQLDataBinder dataBinder(BeanPropertyBinder beanPropertyBinder) {
        new DefaultGraphQLDataBinder(beanPropertyBinder)
    }

    @Singleton
    GraphQLDataBinderManager dataBinderManager(GraphQLDataBinder dataBinder) {
        new DefaultGraphQLDataBinderManager(dataBinder)
    }

    @Singleton
    GraphQLErrorsResponseHandler errorsResponseHandler(@Nullable MessageSource messageSource) {
        new DefaultGraphQLErrorsResponseHandler(messageSource ?: new StaticMessageSource())
    }

    @Singleton
    Schema schema(MappingContext[] mappingContexts,
                  GraphQLDeleteResponseHandler deleteResponseHandler,
                  GraphQLEntityNamingConvention entityNamingConvention,
                  GraphQLTypeManager typeManager,
                  GraphQLDataBinderManager dataBinderManager,
                  GraphQLDataFetcherManager dataFetcherManager,
                  GraphQLInterceptorManager interceptorManager,
                  GraphQLPaginationResponseHandler paginationResponseHandler,
                  GraphQLServiceManager serviceManager,
                  GraphqlConfiguration configuration) {
        Schema schema = new Schema(mappingContexts)
        schema.deleteResponseHandler = deleteResponseHandler
        schema.namingConvention = entityNamingConvention
        schema.typeManager = typeManager
        schema.dataBinderManager = dataBinderManager
        schema.dataFetcherManager = dataFetcherManager
        schema.interceptorManager = interceptorManager
        schema.paginationResponseHandler = paginationResponseHandler
        schema.serviceManager = serviceManager
        schema.dateFormats = configuration.dateFormats.orElse(null)
        schema.dateFormatLenient = configuration.dateFormatLenient
        schema.listArguments = configuration.listArguments.orElse(null)
        schema
    }

    @Singleton
    GraphQLSchema graphQLSchema(Schema schema) {
        schema.generate()
    }

    @Singleton
    GraphQL graphQL(GraphQLSchema schema) {
        new GraphQL(schema)
    }

}
