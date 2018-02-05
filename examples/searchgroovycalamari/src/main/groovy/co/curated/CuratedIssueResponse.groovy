package co.curated

import groovy.transform.CompileStatic
import groovy.transform.ToString

@ToString
@CompileStatic
class CuratedIssueResponse {
    Integer number
    String title
    String summary
    String url
    Date published_at
    Date updated_at
    List<CuratedCategoryResponse> categories = []

    Date getPublishedAt() {
        published_at
    }

    Date getUpdatedAt() {
        updated_at
    }
}

