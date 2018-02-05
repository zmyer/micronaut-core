package com.groovycalamari.services

import co.curated.CuratedCategoryResponse
import co.curated.CuratedIssueResponse
import co.curated.CuratedItem
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.annotations.NonNull

class CuratedItemObservableOnSubscribe implements ObservableOnSubscribe<CuratedItem> {
    public final static List<String> SPONOSORED_CATEGORIES = [
            'logoplacement',
            'jobs',
            'sponsoredlink',
    ]

    CuratedIssueResponse rsp

    CuratedItemObservableOnSubscribe(CuratedIssueResponse rsp) {
        this.rsp = rsp
    }

    @Override
    void subscribe(@NonNull ObservableEmitter<CuratedItem> e) throws Exception {
        for (CuratedCategoryResponse categoryRsp : rsp.categories) {
            if (SPONOSORED_CATEGORIES.contains(categoryRsp.code)) {
                continue
            }
            for (CuratedItem item : categoryRsp.items) {
                if (item.type == 'Text') {
                    continue
                }
                e.onNext(item)
            }
        }
    }
}

