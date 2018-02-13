package com.groovycalamari.services

import co.curated.CuratedIssueResponse
import com.groovycalamari.entities.SearchResult
import groovy.transform.CompileStatic

@CompileStatic
interface HtmlGenerator {
    String renderHTML(String query, List<SearchResult> searchResultList)
    String searchResultAsHtml(SearchResult searchResult)
    String renderHtml(CuratedIssueResponse rsp)
}