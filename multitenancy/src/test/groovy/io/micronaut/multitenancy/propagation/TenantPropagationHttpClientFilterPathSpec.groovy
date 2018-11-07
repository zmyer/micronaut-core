package io.micronaut.multitenancy.propagation

import io.micronaut.context.ApplicationContext
import io.micronaut.context.env.Environment
import io.micronaut.core.annotation.AnnotationMetadataResolver
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.core.util.PathMatcher
import io.micronaut.core.util.StringUtils
import io.micronaut.http.annotation.Filter
import spock.lang.Specification

class TenantPropagationHttpClientFilterPathSpec extends Specification {

    static final SPEC_NAME_PROPERTY = 'spec.name'


    void "defaultTenantnPropagationHttpClientFilter path is /**"() {
        given:
        ApplicationContext context = ApplicationContext.run([
                'micronaut.multitenancy.propagation.enabled': true,
                'micronaut.multitenancy.tenantresolver.httpheader.enabled': true,
                'micronaut.multitenancy.tenantwriter.httpheader.enabled': true,
                (SPEC_NAME_PROPERTY):getClass().simpleName
        ], Environment.TEST)

        when:
        TenantPropagationHttpClientFilter filter = context.getBean(TenantPropagationHttpClientFilter)

        then:
        noExceptionThrown()

        when:
        AnnotationMetadataResolver annotationMetadataResolver = context.getBean(AnnotationMetadataResolver)

        then:
        noExceptionThrown()

        when:
        String[] patterns
        Optional<AnnotationValue<Filter>> filterOpt = annotationMetadataResolver.resolveMetadata(filter).findAnnotation(Filter.class)
        if (filterOpt.isPresent()) {
            AnnotationValue<Filter> filterAnn = filterOpt.get()
            patterns = filterAnn.getValue(String[].class).orElse(StringUtils.EMPTY_STRING_ARRAY)
        }

        then:
        patterns

        when:
        String requestPath = "/invoice"
        boolean matches = PathMatcher.ANT.matches(patterns.first(), requestPath)

        then:
        matches

        cleanup:
        context.stop()
        context.close()
    }

    void "you can customize TenantPropagationHttpClientFilter pattern with micronaut.security.token.propagation.path"() {
        given:
        ApplicationContext context = ApplicationContext.run([
                'micronaut.multitenancy.propagation.enabled': true,
                'micronaut.multitenancy.tenantresolver.httpheader.enabled': true,
                'micronaut.multitenancy.tenantwriter.httpheader.enabled': true,
                'micronaut.multitenancy.propagation.path': '/books/**',
                (SPEC_NAME_PROPERTY):getClass().simpleName
        ], Environment.TEST)

        when:
        TenantPropagationHttpClientFilter filter = context.getBean(TenantPropagationHttpClientFilter)

        then:
        noExceptionThrown()

        when:
        AnnotationMetadataResolver annotationMetadataResolver = context.getBean(AnnotationMetadataResolver)

        then:
        noExceptionThrown()

        when:
        String[] patterns
        Optional<AnnotationValue<Filter>> filterOpt = annotationMetadataResolver.resolveMetadata(filter).findAnnotation(Filter.class)
        if (filterOpt.isPresent()) {
            AnnotationValue<Filter> filterAnn = filterOpt.get()
            patterns = filterAnn.getValue(String[].class).orElse(StringUtils.EMPTY_STRING_ARRAY)
        }

        then:
        patterns

        when:
        String requestPath = "/books/1"
        boolean matches = PathMatcher.ANT.matches(patterns.first(), requestPath)

        then:
        matches

        when:
        requestPath = "/invoice"
        matches = PathMatcher.ANT.matches(patterns.first(), requestPath)

        then:
        !matches

        cleanup:
        context.stop()
        context.close()
    }
}
