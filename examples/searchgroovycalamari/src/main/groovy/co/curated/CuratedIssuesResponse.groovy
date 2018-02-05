package co.curated

import groovy.transform.CompileStatic
import groovy.transform.ToString

@ToString
@CompileStatic
class CuratedIssuesResponse {
    Integer page
    Integer total_pages
    Integer total_results
    List<CuratedIssueSummary> issues = []

    Integer getTotalPages() {
        total_pages
    }

    Integer getTotalResults() {
        total_results
    }
}