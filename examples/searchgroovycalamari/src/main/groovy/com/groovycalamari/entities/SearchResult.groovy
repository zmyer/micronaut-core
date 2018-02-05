package com.groovycalamari.entities

import groovy.transform.ToString

@ToString
class SearchResult {
    String title
    String url
    String description
    Float titleSimilarity
    Float descriptionSimilarity
    String query

    Float getSimilarity() {
        (titleSimilarity + descriptionSimilarity) as Float
    }
}
