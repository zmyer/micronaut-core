package co.curated

import spock.lang.IgnoreIf
import spock.lang.Specification

class CuratedApiSpec extends Specification {

    @IgnoreIf( { !System.getProperty('CURATED_PUBLICATION_KEY') || !System.getProperty('CURATED_API_KEY') } )
    def "findAllIssuesSummaries"() {
        given:
        def publicationKey = System.getProperty('CURATED_PUBLICATION_KEY')
        def apiKey = System.getProperty('CURATED_API_KEY')
         def api = new CuratedApi(publicationKey, apiKey)

        when:
        List<CuratedIssueSummary> l = api.findAllIssuesSummaries()

        then:
        l
        l.size() >= 80

        when:
        List<CuratedIssueResponse> issues = api.findAllCuratedIssues()

        then:
        l
        l.size() == issues.size()
    }
}
