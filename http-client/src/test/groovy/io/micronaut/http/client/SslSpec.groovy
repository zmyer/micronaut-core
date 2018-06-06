package io.micronaut.http.client

import io.micronaut.context.ApplicationContext
import spock.lang.Specification

class SslSpec extends Specification {

    void "test that clients work with self signed certificates"() {
        given:
        ApplicationContext ctx = ApplicationContext.run()
        HttpClient client = ctx.createBean(HttpClient, new URL("https://httpbin.org"))

        expect:
        client.toBlocking().retrieve('/get')

        cleanup:
        ctx.close()
        client.close()
    }
}
