package co.curated.particleclient

import co.curated.CuratedApiConstants
import co.curated.CuratedApiUris
import co.curated.CuratedIssueResponse
import co.curated.CuratedIssuesResponse
import io.reactivex.Flowable
import org.particleframework.context.annotation.Value
import org.particleframework.http.HttpRequest
import org.particleframework.http.HttpResponse
import org.particleframework.http.client.Client
import org.particleframework.http.client.rxjava2.RxHttpClient

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CuratedApiClientParticle implements CuratedApiUris, CuratedApiClient  {

    @Value('curated.apiVersion')
    String apiVersion

    @Value('curated.publicationKey')
    String publicationKey

    @Inject
    @Client(CuratedApiConstants.CURATED_API_URL)
    RxHttpClient client

    @Override
    Flowable<HttpResponse<CuratedIssuesResponse>> issues() {
        client.exchange(HttpRequest.GET(issuesUri()), CuratedIssuesResponse)
    }

    @Override
    Flowable<HttpResponse<CuratedIssueResponse>> issueByNumber(int issueNumber) {
        client.exchange(HttpRequest.GET(issueByNumberUri(issueNumber)), CuratedIssueResponse)
    }
}
