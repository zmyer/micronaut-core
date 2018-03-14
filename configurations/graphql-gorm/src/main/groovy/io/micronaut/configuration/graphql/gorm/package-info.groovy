@io.micronaut.context.annotation.Configuration
@Requirements([
        @Requires(classes = [Datastore, javax.persistence.Entity]),
        @Requires(entities = [Entity, javax.persistence.Entity])
])
package io.micronaut.configuration.graphql.gorm

import grails.gorm.annotation.Entity
import io.micronaut.context.annotation.Requirements
import io.micronaut.context.annotation.Requires
import org.grails.datastore.mapping.core.Datastore