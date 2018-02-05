package co.curated

import groovy.transform.CompileStatic
import groovy.transform.ToString

@ToString
@CompileStatic
class CuratedCategoryResponse {
    String code
    String name
    List<CuratedItem> items = []
}

