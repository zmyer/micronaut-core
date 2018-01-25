package com.objectcomputing.training

import com.objectcomputing.training.model.Offering
import groovy.transform.CompileStatic

@CompileStatic
interface TrainingUseCase {
    Set<Offering> findAllOfferings()
}