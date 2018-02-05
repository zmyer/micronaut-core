package co.curated

import groovy.transform.CompileStatic

@CompileStatic
trait CuratedApiHeaders {

    abstract String getApiKey()

    Map<String, String> headers() {
        ['Authorization': "Token token=\"${apiKey}\"" as String,
         'Accept': 'application/json',
         'Content-Type': 'application/json']
    }

    String header(String headerName) {
        Map headers = headers()
        if (headers.containsKey(headerName) ) {
            return headers[headerName]
        }
        null
    }
}
