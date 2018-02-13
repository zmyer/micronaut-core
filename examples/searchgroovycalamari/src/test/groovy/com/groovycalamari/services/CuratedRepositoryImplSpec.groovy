package com.groovycalamari.services

import co.curated.CuratedIssueResponse
import spock.lang.Specification

class CuratedRepositoryImplSpec extends Specification {

    def "test latest"() {
        given:
        CuratedRepository curatedRepository = new CuratedRepositoryImpl()
        curatedRepository.curatedIssues[10] = new CuratedIssueResponse(number: 10)
        curatedRepository.curatedIssues[15] = new CuratedIssueResponse(number: 15)
        curatedRepository.curatedIssues[2] = new CuratedIssueResponse(number: 2)

        expect:
        15 == curatedRepository.findLatest()
    }
}
