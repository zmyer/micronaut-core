package com.groovycalamari.services

import com.groovycalamari.entities.SearchResult
import com.groovycalamari.utils.MarkdownUtil
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.xml.MarkupBuilder
import javax.inject.Singleton

@Singleton
@CompileStatic
class HtmlGenerationServiceImpl implements HtmlGenerator {
    String filename = '/search.html'

    @Override
    String renderHTML(String query, List<SearchResult> searchResultList) {
        String text = getClass().getResource(filename).text

        String htmlItemBlock
        if ( searchResultList) {
            List<String> htmlItems = searchResultList.collect { SearchResult searchResult ->
                searchResultAsHtml(searchResult)
            }
            htmlItemBlock = htmlItems.join('<hr class="item__line">\n')
        } else {
            htmlItemBlock  = noResultsHTML(query ? 'No results found' : '')
        }

        text = text.replace('###query###', query)
        text = text.replace('###items###', htmlItemBlock)
        text
    }

    @Override
    @CompileDynamic
    String searchResultAsHtml(SearchResult searchResult) {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)
        html.div(class:"item item--issue item--link") {
            h3(class: "item__title") {
                a(href: searchResult.url, searchResult.title)
            }
            mkp.yieldUnescaped MarkdownUtil.htmlFromMarkdown(searchResult.description)
        }
        writer.toString()
    }

    @CompileDynamic
    String noResultsHTML(String title = '') {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)
        html.div(class:"item item--issue item--link") {
            h3(class: "item__title", title)
        }
        writer.toString()
    }
}
