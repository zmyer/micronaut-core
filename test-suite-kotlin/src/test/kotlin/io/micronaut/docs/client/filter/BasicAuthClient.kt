package io.micronaut.docs.client.filter

//tag::class[]
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client

@BasicAuth // <1>
@Client("/message")
interface BasicAuthClient {

    @Get
    fun getMessage(): String
}
//end::class[]
