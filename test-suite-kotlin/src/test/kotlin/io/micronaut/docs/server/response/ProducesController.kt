package io.micronaut.docs.server.response

//tag::imports[]
import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
//end::imports[]

@Requires(property = "spec.name", value = "producesspec")
//tag::clazz[]
@Controller("/produces")
class ProducesController {

    @Get // <1>
    fun index(): HttpResponse<*> {
        return HttpResponse.ok<Any>().body("{\"msg\":\"This is JSON\"}")
    }

    @Produces(MediaType.TEXT_HTML)
    @Get("/html") // <2>
    fun html(): String {
        return "<html><title><h1>HTML</h1></title><body></body></html>"
    }

    @Get(value = "/xml", produces = [MediaType.TEXT_XML]) // <3>
    fun xml(): String {
        return "<html><title><h1>XML</h1></title><body></body></html>"
    }
}
//end::clazz[]
