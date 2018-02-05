package com.groovycalamari.services

import co.curated.CuratedItem
import groovy.transform.CompileStatic

@CompileStatic
interface CuratedRepository {
    List<CuratedItem> findAll()
}