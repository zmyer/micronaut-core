package com.groovycalamari.services

import co.curated.CuratedIssueResponse
import com.groovycalamari.entities.SearchResult
import com.groovycalamari.utils.MarkdownUtil
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.xml.MarkupBuilder
import javax.inject.Singleton
import java.text.SimpleDateFormat

@Singleton
@CompileStatic
class HtmlGenerationServiceImpl implements HtmlGenerator {
    String filename = '/search.html'

    @Override
    String renderHTML(Integer latest, String query, List<SearchResult> searchResultList) {
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
        text = text.replace('{latest}', "$latest".toString())
        text = text.replace('{query}', query)
        text = text.replace('{items}', htmlItemBlock)
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

    static String[] suffixes = ['th', 'st', 'nd', 'rd', 'th', 'th', 'th', 'th', 'th', 'th',
                //    10    11    12    13    14    15    16    17    18    19
                'th', 'th', 'th', 'th', 'th', 'th', 'th', 'th', 'th', 'th',
                //    20    21    22    23    24    25    26    27    28    29
                'th', 'st', 'nd', 'rd', 'th', 'th', 'th', 'th', 'th', 'th',
                //    30    31
                'th', 'st' ] as String[]




    @CompileDynamic
    String curatedIssueAsHtml(CuratedIssueResponse curatedIssue) {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)

        html.li(class: 'item') {
            a(href: "/issues/${curatedIssue.number}#start") {
                div(class: 'item__body') {
                    h2(class: 'item__heading', "Issue ${curatedIssue.number}")
                    p(class: 'item__title',curatedIssue.summary)
                    h4(class: 'item__footer') {
                        time(class: 'published', datetime: new SimpleDateFormat('yyyy-MM-dd').format(curatedIssue.published_at)) {
                            span {
                                mkp.yield new SimpleDateFormat('dd').format(curatedIssue.published_at)
                                mkp.yield suffixes[Integer.parseInt(new SimpleDateFormat("d").format(curatedIssue.published_at))]
                                mkp.yield " ${new SimpleDateFormat('MMM').format(curatedIssue.published_at)}".toString()
                            }
                            mkp.yield " ${new SimpleDateFormat('yyyy').format(curatedIssue.published_at)}".toString()
                        }
                    }
                }
            }
        }
        writer.toString()
    }

    @Override
    String renderHtml(CuratedIssueResponse rsp) {
        return null
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
