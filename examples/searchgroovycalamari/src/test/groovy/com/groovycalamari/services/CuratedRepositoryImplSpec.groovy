package com.groovycalamari.services

import co.curated.CuratedIssueResponse
import spock.lang.Shared
import spock.lang.Specification

class CuratedRepositoryImplSpec extends Specification {

    @Shared
    CuratedRepositoryImpl curatedRepository = new CuratedRepositoryImpl()

    def setupSpec() {
        curatedRepository.curatedIssues[10] = new CuratedIssueResponse(number: 10)
        curatedRepository.curatedIssues[15] = new CuratedIssueResponse(number: 15)
        curatedRepository.curatedIssues[2] = new CuratedIssueResponse(number: 2)
    }

    def "test latest"() {
        expect:
        15 == curatedRepository.findLatest()
    }

    def "test orderedKeys"() {
        expect:
        [15, 10, 2] == curatedRepository.orderedKeys()
    }

    def "test findAll"() {
        expect:
        [15, 10] == curatedRepository.findAll(0, 2)*.number

        and:
        [15, 10, 2] == curatedRepository.findAll(0, 4)*.number
    }
}
