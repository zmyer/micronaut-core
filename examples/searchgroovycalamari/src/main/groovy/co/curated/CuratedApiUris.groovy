package co.curated

import groovy.transform.CompileStatic

@CompileStatic
trait CuratedApiUris {

    abstract String getPublicationKey()
    abstract String getApiVersion()

    String pathWithSubpath(String path) {
        if ( !path.startsWith('/') ) {
            throw new IllegalArgumentException('path should start with /')
        }
        "/${publicationKey}/api/${apiVersion}${path}"
    }

    String issuesUri() {
        pathWithSubpath('/issues')
    }

    String issueByNumberUri(int number) {
        pathWithSubpath("/issues/$number")
    }
}