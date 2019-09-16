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
package io.micronaut.http.client.reactor

import io.micronaut.context.ApplicationContext
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client
import io.micronaut.runtime.server.EmbeddedServer
import reactor.core.publisher.Flux
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class ClientStreamSpec extends Specification {

    @Shared @AutoCleanup EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer)
    @Shared BookClient bookClient = embeddedServer.applicationContext.getBean(BookClient)

    void "test stream array of json objects"() {
        when:
        List<Book> books = bookClient.arrayStream().collectList().block()

        then:
        books.size() == 2
        books[0].title == "The Stand"
        books[1].title == "The Shining"
    }

    void "test stream json stream of objects"() {
        when:
        List<Book> books = bookClient.jsonStream().collectList().block()

        then:
        books.size() == 2
        books[0].title == "The Stand"
        books[1].title == "The Shining"

    }


    @Client('/reactor/stream')
    static interface BookClient extends BookApi {

    }

    @Controller("/reactor/stream")
    static class StreamController implements BookApi {

        @Override
        Flux<Book> arrayStream() {
            return Flux.just(
                    new Book(title: "The Stand"),
                    new Book(title: "The Shining"),
            )
        }

        @Override
        Flux<Book> jsonStream() {
            return Flux.just(
                    new Book(title: "The Stand"),
                    new Book(title: "The Shining"),
            )
        }
    }

    static interface BookApi {
        @Get("/array")
        Flux<Book> arrayStream()

        @Get(value = "/json", processes = MediaType.APPLICATION_JSON_STREAM)
        Flux<Book> jsonStream()
    }

    static class Book {
        String title
    }
}

