package co.curated.particleclient

import co.curated.CuratedIssueResponse
import co.curated.CuratedIssuesResponse
import groovy.transform.CompileStatic
import io.reactivex.Flowable
import org.particleframework.http.HttpResponse

@CompileStatic
interface CuratedApiClient {
    Flowable<HttpResponse<CuratedIssueResponse>> issueByNumber(int issueNumber)
    Flowable<HttpResponse<CuratedIssuesResponse>> issues()
}