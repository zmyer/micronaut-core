package com.groovycalamari.services

import com.groovycalamari.entities.SearchResult
import groovy.transform.CompileStatic
import io.reactivex.Observable

@CompileStatic
interface SearchService {
    List<SearchResult> search(String query)
}