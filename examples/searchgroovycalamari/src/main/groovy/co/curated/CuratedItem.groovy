package co.curated

import groovy.transform.CompileStatic
import groovy.transform.ToString

@ToString
@CompileStatic
class CuratedItem {
    String identifier
    String type
    String title
    String description
    String footer
    String url_domain
    String url
    List<CuratedEmbeddedLink> embedded_links = []

    String getUrlDomain() {
        url_domain
    }

    List<CuratedEmbeddedLink> getEmbeddedLinks() {
        embedded_links
    }
}
