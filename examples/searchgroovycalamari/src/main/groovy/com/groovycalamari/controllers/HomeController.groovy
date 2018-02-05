package com.groovycalamari.controllers

import com.groovycalamari.entities.SearchResult
import com.groovycalamari.services.HtmlGenerator
import com.groovycalamari.services.SearchService
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.particleframework.context.ApplicationContext
import org.particleframework.http.HttpResponse
import org.particleframework.http.MediaType
import org.particleframework.http.annotation.Controller
import org.particleframework.http.annotation.Get
import org.particleframework.http.annotation.Post
import org.particleframework.http.annotation.Produces
import org.particleframework.runtime.context.scope.refresh.RefreshEvent
import javax.inject.Inject
import javax.inject.Singleton

import static org.particleframework.http.HttpResponse.ok

@Slf4j
@CompileStatic
@Controller('/')
@Singleton
class HomeController {

    @Inject
    HtmlGenerator htmlGenerator

    @Inject
    SearchService searchService

    @Inject
    ApplicationContext applicationContext

    @Produces(MediaType.TEXT_HTML)
    @Get('/')
    String index(Optional<String> query) {
        String html
        if ( query.isPresent() ) {
            List<SearchResult> searchResultList = searchService.search(query.get())
            html = htmlGenerator.renderHTML(query.get(), searchResultList)
        } else {
            html = htmlGenerator.renderHTML('', [])

        }
        html
    }

    @Post("/evict")
    HttpResponse<Map<String, String>> evict() {
        applicationContext.publishEvent(new RefreshEvent())
        ok([msg: 'OK']) as HttpResponse<Map<String, String>>
    }
}
