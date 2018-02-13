package com.groovycalamari.services

import co.curated.CuratedIssueResponse
import co.curated.CuratedItem
import groovy.transform.CompileStatic

@CompileStatic
interface CuratedRepository {

    Integer findLatest()
    
    List<CuratedItem> findAll()

    CuratedIssueResponse findIssue(Integer number)

    List<CuratedIssueResponse> findAll(Integer offset, Integer max)
}