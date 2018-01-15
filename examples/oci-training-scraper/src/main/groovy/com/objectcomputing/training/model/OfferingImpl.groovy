package com.objectcomputing.training.model

import groovy.transform.CompileStatic
import groovy.transform.ToString

@ToString
@CompileStatic
class Offering {
    Long id
    String course
    String dates
    String time
    String instructors
    String hours
    Track track
    String enrollmentLink
    boolean soldOut
}
