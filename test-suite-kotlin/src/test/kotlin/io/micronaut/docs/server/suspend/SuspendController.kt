package io.micronaut.docs.server.suspend

import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Status
import kotlinx.coroutines.delay

@Controller("/suspend")
class SuspendController {

    // tag::suspend[]
    @Get("/simple")
    suspend fun simple(): String { // <1>
        return "Hello"
    }
    // end::suspend[]

    // tag::suspendDelayed[]
    @Get("/delayed")
    suspend fun delayed(): String { // <1>
        delay(1) // <2>
        return "Delayed"
    }
    // end::suspendDelayed[]

    // tag::suspendStatus[]
    @Status(HttpStatus.CREATED) // <1>
    @Get("/status")
    suspend fun status(): Unit {
    }
    // end::suspendStatus[]

    // tag::suspendStatusDelayed[]
    @Status(HttpStatus.CREATED)
    @Get("/statusDelayed")
    suspend fun statusDelayed(): Unit {
        delay(1)
    }
    // end::suspendStatusDelayed[]
}