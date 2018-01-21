package com.objectcomputing.training.model

import groovy.transform.CompileStatic
import groovy.transform.ToString

@CompileStatic
interface Offering {
    String getCourse()
    String getDates()
    String getEnrollmentLink()
    String getInstructors()
    String getHours()
}
