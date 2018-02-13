package com.groovycalamari.services

import co.curated.CuratedCategoryResponse
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

    Map<Integer, CuratedIssueResponse> curatedIssues = [:]

    @Inject
    CuratedApiClient curatedApiClient

    @PostConstruct
    void initialize() {
        fetchIssues()
    }

    @Override
    Integer findLatest() {
        List<Integer> keys = orderedKeys()
        if ( !keys ) {
            return null
        }
        keys.first()
    }

    List<Integer> orderedKeys() {
        Set<Integer> keys = curatedIssues.keySet()
        if ( !keys ) {
            return null
        }
        keys.sort().reverse()
    }

    @Override
    List<CuratedIssueResponse> findAll(Integer offset, Integer max) {
        List<Integer> keys = orderedKeys()
        List<Integer> keysSubList = keys.subList(Math.max(offset, 0), Math.min(max, keys.size()))
        keysSubList.collect { Integer number ->
            curatedIssues[number]
        }
    }

    @Override
    CuratedIssueResponse findIssue(Integer number) {
        curatedIssues[number]
    }

    @Override
    List<CuratedItem> findAll() {

        List<CuratedItem> result = []
        for ( CuratedIssueResponse rsp : curatedIssues.values() ) {
            for (CuratedCategoryResponse categoryRsp : rsp.categories) {
                if (CuratedItemObservableOnSubscribe.SPONOSORED_CATEGORIES.contains(categoryRsp.code)) {
                    continue
                }
                for (CuratedItem item : categoryRsp.items) {
                    if (item.type == 'Text') {
                        continue
                    }
                    result << item
                }
            }
        }
        result
    }

    void fetchIssues() {
        curatedIssues.clear()
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
                                    if ( curatedIssueResponse != null) {
                                        curatedIssues[curatedIssueResponse.number] = curatedIssueResponse
                                    }
                                }
                            }
                        })
                    }
                }
            }
        })
    }
}
