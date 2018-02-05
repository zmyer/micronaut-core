package com.groovycalamari.services

import co.curated.CuratedIssueResponse
import co.curated.CuratedIssuesResponse
import co.curated.CuratedItem
import co.curated.particleclient.CuratedApiClient
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import org.particleframework.http.HttpResponse
import org.particleframework.http.HttpStatus
import org.particleframework.runtime.context.scope.Refreshable

import javax.annotation.PostConstruct
import javax.inject.Inject

@Refreshable
@Slf4j
@CompileStatic
class CuratedRepositoryImpl implements CuratedRepository {
    List<CuratedItem> items = []

    @Inject
    CuratedApiClient curatedApiClient

    @PostConstruct
    void initialize() {
        fetchIssues()
    }

    @Override
    List<CuratedItem> findAll() {
        new ArrayList<>(items)
    }

    void fetchIssues() {

        items.clear()
        log.info('fetching issues')

        Flowable<HttpResponse<CuratedIssuesResponse>> issuesHttpResponseFlowable = curatedApiClient.issues()
        issuesHttpResponseFlowable.subscribe(new Consumer<HttpResponse<CuratedIssuesResponse>>() {
            @Override
            void accept(HttpResponse<CuratedIssuesResponse> curatedIssuesResponseHttpResponse) throws Exception {

                if (curatedIssuesResponseHttpResponse.status == HttpStatus.OK) {
                    CuratedIssuesResponse curatedIssuesResponse = curatedIssuesResponseHttpResponse.body()
                    for (int index = 1; index <= curatedIssuesResponse.totalResults; index++) {
                        curatedApiClient.issueByNumber(index).subscribe(new Consumer<HttpResponse<CuratedIssueResponse>>() {
                            @Override
                            void accept(HttpResponse<CuratedIssueResponse> curatedIssueResponseHttpResponse) throws Exception {
                                if (curatedIssueResponseHttpResponse.status == HttpStatus.OK) {
                                    CuratedIssueResponse curatedIssueResponse = curatedIssueResponseHttpResponse.body()
                                    log.info 'fetched issue #{}', curatedIssueResponse.number
                                    Observable.create(new CuratedItemObservableOnSubscribe(curatedIssueResponse)).subscribe(new Consumer<CuratedItem>() {
                                        @Override
                                        void accept(CuratedItem curatedItem) throws Exception {
                                            if ( curatedItem != null ) {
                                                items << curatedItem
                                            }
                                        }
                                    })
                                }
                            }
                        })
                    }
                }
            }
        })
    }
}
