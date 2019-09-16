/*
 * Copyright 2017-2019 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.http.server.netty.interceptor

import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MutableHttpRequest
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Filter
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.http.filter.HttpServerFilter
import io.micronaut.http.filter.ServerFilterChain
import io.micronaut.http.server.netty.AbstractMicronautSpec
import io.reactivex.Flowable
import org.reactivestreams.Publisher

/**
 * @author Graeme Rocher
 * @since 1.0
 */
class HttpFilterSpec extends AbstractMicronautSpec {

    void "test interceptor execution and order - write replacement"() {
        when:
        rxClient.retrieve("/secure").blockingFirst()

        then:
        def e = thrown(HttpClientResponseException)
        e.status == HttpStatus.FORBIDDEN
    }

    void "test interceptor execution and order - proceed"() {
        when:
        HttpResponse<String> response = rxClient.exchange("/secure?username=fred", String).blockingFirst()

        then:
        response.status == HttpStatus.OK
        response.headers.get("X-Test") == "Foo Test"
        response.body.isPresent()
        response.body.get() == "Authenticated: fred"
    }

    void "test a filter on the root url"() {
        when:
        HttpResponse response = rxClient.exchange("/").blockingFirst()

        then:
        response.status == HttpStatus.OK
        response.headers.get("X-Root-Filter") == "processed"
    }

    @Requires(property = 'spec.name', value = "HttpFilterSpec")
    @Filter("/**")
    static class RootFilter implements HttpServerFilter {

        @Override
        Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request, ServerFilterChain chain) {
            return Flowable.fromPublisher(chain.proceed(request)).doOnNext({ response ->
                response.header("X-Root-Filter", "processed")
            })
        }
    }

    @Requires(property = 'spec.name', value = "HttpFilterSpec")
    @Controller
    static class RootController {

        @Get
        HttpResponse root() {
            HttpResponse.ok()
        }

    }
}
