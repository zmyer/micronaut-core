package io.micronaut.security.denyall

import io.micronaut.context.ApplicationContext
import io.micronaut.context.env.Environment
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.runtime.server.EmbeddedServer
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class DenyAllSpec extends Specification {
    static final String SPEC_NAME_PROPERTY = 'spec.name'

    public static final String controllerPath = '/denyall'

    @Shared
    @AutoCleanup
    EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer, [
            (SPEC_NAME_PROPERTY): DenyAllSpec.class.simpleName,
            'micronaut.security.enabled': true,
    ], Environment.TEST)

    @Shared
    @AutoCleanup
    RxHttpClient client = embeddedServer.applicationContext.createBean(RxHttpClient, embeddedServer.getURL())

    void "DenyAll collaborators are loaded"() {
        when:
        embeddedServer.applicationContext.getBean(BookController)

        then:
        noExceptionThrown()

        when:
        embeddedServer.applicationContext.getBean(AuthenticationProviderUserPassword)

        then:
        noExceptionThrown()
    }

    def "@DenyAll annotation is equivalent to @Secured('denyAll()')"() {
        when: 'accessing as anonymous an endpoint @Secured("isAnonymous()")'
        client.toBlocking().exchange(HttpRequest.GET("${controllerPath}/index"))

        then:
        noExceptionThrown()

        when: 'accessing as anonymous a @DenyAll endpoint'
        client.toBlocking().exchange(HttpRequest.GET("${controllerPath}/denied"))

        then: '401 is returned'
        def e = thrown(HttpClientResponseException)
        e.response.status == HttpStatus.UNAUTHORIZED

        when: 'when authenticated'
        client.toBlocking().exchange(HttpRequest.GET("${controllerPath}/denied").basicAuth("user", "password"))

        then: 'user is denied with 403'
        e = thrown(HttpClientResponseException)
        e.response.status == HttpStatus.FORBIDDEN
    }

    def "@Secured('denyAll()') endpoints throw 401"() {
        when: 'accessing as anonymous an endpoint @Secured("isAnonymous()")'
        client.toBlocking().exchange(HttpRequest.GET("${controllerPath}/index"))

        then:
        noExceptionThrown()

        when: 'accessing as anonymous a @Secured("denyAll()") endpoint'
        client.toBlocking().exchange(HttpRequest.GET("${controllerPath}/secureddenied"))

        then: '401 is returned'
        def e = thrown(HttpClientResponseException)
        e.response.status == HttpStatus.UNAUTHORIZED
    }
}
