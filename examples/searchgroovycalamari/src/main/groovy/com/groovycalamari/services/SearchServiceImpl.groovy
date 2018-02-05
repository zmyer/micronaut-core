package com.groovycalamari.services

import co.curated.CuratedItem
import com.groovycalamari.entities.SearchResult
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.particleframework.context.annotation.Value
import org.simmetrics.StringMetric
import org.simmetrics.builders.StringMetricBuilder
import org.simmetrics.metrics.Levenshtein
import org.simmetrics.simplifiers.Simplifiers
import javax.inject.Inject
import javax.inject.Singleton

@Slf4j
@Singleton
@CompileStatic
class SearchServiceImpl implements SearchService {

    @Inject
    CuratedRepository curatedRepository

    StringMetric stringMetric = StringMetricBuilder.with(new Levenshtein())
            .simplify(Simplifiers.toLowerCase(Locale.ENGLISH))
            .simplify(Simplifiers.replaceNonWord())
    //.tokenize(Tokenizers.whitespace())
            .build()

    public static final List<String> KEYWORDS = [
            'grails',
            'geb',
            'griffon',
            'gorm',
            'ratpack',
    ]

    public static final float TITLE_BOOST = 0.5

    @Override
    List<SearchResult> search(String query) {
        List<CuratedItem> curatedItemList = curatedRepository.findAll()
        filterItems(curatedItemList, query)
    }

    List<SearchResult> filterItems(List<CuratedItem> items, String query) {
        List<SearchResult> results = []

        for ( CuratedItem item : items ) {

            if ( matchesEveryQueryWords(item, query) ) {
                float titleSimilarity = similarity(item.title, query)
                if ( titleSimilarity > 0 ) {
                    titleSimilarity += TITLE_BOOST
                }
                SearchResult searchResult = new SearchResult(title: item.title,
                        url: item.url,
                        description: item.description,
                        titleSimilarity: titleSimilarity,
                        descriptionSimilarity: similarity(item.description, query),
                        query: query)
                if (searchResult.similarity) {
                    results << searchResult
                }
            }
        }

        results
    }

    float similarity(String text, String query) {
        if ( !query ) {
            return
        }
        String lowercaseText = text.toLowerCase()
        String[] arr = query.split(' ')
        float similarity = 0
        float itemBoost = 0
        for ( String word : arr ) {
            similarity += itemBoost + stringMetric.compare(lowercaseText, word.toLowerCase())
        }
        similarity
    }


    boolean matchesEveryQueryWords(CuratedItem item, String query) {
        String[] arr = query.split(' ')
        arr.every { String queryWord ->
            String word = processword(queryWord).toLowerCase()
            item.title.toLowerCase().contains(word) || item.description.toLowerCase().contains(word)
        }
    }

    String processword(String word) {
        if ( !word ) {
            return word
        }
        if (KEYWORDS.contains(word.toLowerCase()) ) {
            return word
        }
        if ( word.endsWith('s') || word.endsWith('S') ) {
            return word.substring(0, (word.length() -1) )
        }
        word
    }
}

