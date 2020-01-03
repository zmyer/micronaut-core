package io.micronaut.docs.server.endpoint

//tag::endpointImport[]
import io.micronaut.management.endpoint.annotation.Endpoint
//end::endpointImport[]

//tag::readImport[]
import io.micronaut.management.endpoint.annotation.Read
//end::readImport[]

//tag::mediaTypeImport[]
import io.micronaut.http.MediaType
import io.micronaut.management.endpoint.annotation.Selector

//end::mediaTypeImport[]

//tag::writeImport[]
import io.micronaut.management.endpoint.annotation.Write
//end::writeImport[]

import javax.annotation.PostConstruct
import java.util.*

//tag::endpointClassBegin[]
@Endpoint(id = "date", prefix = "custom", defaultEnabled = true, defaultSensitive = false)
class CurrentDateEndpoint {
    //end::endpointClassBegin[]

    //tag::methodSummary[]
    //.. endpoint methods
    //end::methodSummary[]

    //tag::currentDate[]
    private var currentDate: Date? = null
    //end::currentDate[]

    @PostConstruct
    fun init() {
        currentDate = Date()
    }

    //tag::simpleRead[]
    @Read
    fun currentDate(): Date? {
        return currentDate
    }
    //end::simpleRead[]

    //tag::readArg[]
    @Read(produces = [MediaType.TEXT_PLAIN]) //<1>
    fun currentDatePrefix(@Selector prefix: String): String {
        return "$prefix: $currentDate"
    }
    //end::readArg[]

    //tag::simpleWrite[]
    @Write
    fun reset(): String {
        currentDate = Date()

        return "Current date reset"
    }
    //end::simpleWrite[]
    //tag::endpointClassEnd[]
}
//end::endpointClassEnd[]

