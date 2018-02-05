package co.curated

import groovy.transform.CompileStatic
import groovy.transform.ToString

@ToString
@CompileStatic
class CuratedEmbeddedLink {
    String identifier
    String title
    String url_domain
    String url

    String getUrlDomain() {
        url_domain
    }
}
