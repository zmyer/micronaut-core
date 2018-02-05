package co.curated

import co.curated.particleclient.CuratedApiClient
import co.curated.particleclient.CuratedApiClientParticle
import io.reactivex.Flowable
import org.particleframework.http.HttpResponse
import org.particleframework.http.HttpStatus
import spock.lang.IgnoreIf
import spock.lang.Shared
import spock.lang.Specification

class CuratedApiClientSpec extends Specification {

    @Shared
    CuratedApiClient curatedApiClient

    def setup() {
        String publicationKey = System.getProperty('CURATED_PUBLICATION_KEY')
        String apiKey = System.getProperty('CURATED_API_KEY')
        curatedApiClient = new CuratedApiClientParticle(publicationKey, apiKey)
    }

    @IgnoreIf( { !System.getProperty('CURATED_PUBLICATION_KEY') || !System.getProperty('CURATED_API_KEY') } )
    def "Fetch Issues"() {
        when:
        Flowable<HttpResponse<CuratedIssuesResponse>> flowable = curatedApiClient.issues()
        HttpResponse<CuratedIssuesResponse> response = flowable.blockingFirst()

        then:
        response.status == HttpStatus.OK

        when:
        CuratedIssuesResponse curatedIssueResponse = response.body()

        then:
        curatedIssueResponse.issues
        curatedIssueResponse.totalResults > 100
        curatedIssueResponse.totalPages > 10
        curatedIssueResponse.issues.size() == 10
    }

    @IgnoreIf( { !System.getProperty('CURATED_PUBLICATION_KEY') || !System.getProperty('CURATED_API_KEY') } )
    def "Fetch Issues by number"() {
        when:
        Flowable<HttpResponse<CuratedIssueResponse>> flowable = curatedApiClient.issueByNumber(111)
        HttpResponse<CuratedIssueResponse> response = flowable.blockingFirst()

        then:
        response.status == HttpStatus.OK

        when:
        CuratedIssueResponse curatedIssueResponse = response.body()

        then:
        curatedIssueResponse.number == 111
        curatedIssueResponse.categories.size() > 2
    }
}
