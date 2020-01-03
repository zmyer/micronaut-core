package io.micronaut.docs.writable;

//tag::imports[]

import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;
import io.micronaut.core.io.Writable;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.server.exceptions.HttpServerException;
//end::imports[]

//tag::clazz[]
@Controller("/template")
class TemplateController {

    private final SimpleTemplateEngine templateEngine = new SimpleTemplateEngine()
    private final Template template

    TemplateController() {
        template = initTemplate() // <1>
    }

    @Get(value = "/welcome", produces = MediaType.TEXT_PLAIN)
    Writable render() { // <2>
        { writer ->
            template.make( // <3>
                    CollectionUtils.mapOf(
                            "firstName", "Fred",
                            "lastName", "Flintstone"
                    )
            ).writeTo(writer)
        }
    }

    private Template initTemplate() {
        Template template
        try {
            template = templateEngine.createTemplate(
                    'Dear $firstName $lastName. Nice to meet you.'
            )
        } catch (Exception e) {
            throw new HttpServerException("Cannot create template")
        }
        template
    }
}
//end::clazz[]