package io.micronaut.http.client

import io.micronaut.context.ApplicationContext
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.validation.Validated
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class ConventionsSpec extends Specification {

    @Shared @AutoCleanup EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer)

    void 'test convention mappings for client'() {
        given:
        HelloConventionClient client = embeddedServer.getApplicationContext().getBean(HelloConventionClient)

        expect:
        client.fooBar() == 'good'
    }

    void 'test convention mappings'() {
        given:
        RxHttpClient client = embeddedServer.getApplicationContext().createBean(RxHttpClient, embeddedServer.getURL())

        expect:
        client.toBlocking().retrieve('/hello-convention') == 'good'

        cleanup:
        client.close()

    }

    void 'test convention mappings with validation'() {
        given:
        RxHttpClient client = embeddedServer.getApplicationContext().createBean(RxHttpClient, embeddedServer.getURL())

        expect:
        client.toBlocking().retrieve('/hello-validated') == 'good'

        cleanup:
        client.close()
    }

    @Client('/hello-convention')
    static interface HelloConventionClient {
        @Get
        String fooBar()
    }

    @Controller('/hello-convention')
    static class HelloConventionController {
        @Get
        String fooBar() {
            "good"
        }
    }

    @Controller('/hello-validated')
    @Validated
    static class HelloValidatedController {
        @Get
        String fooBar() {
            "good"
        }
    }
}
