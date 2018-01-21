package com.objectcomputing.training.model

import groovy.transform.CompileStatic

@CompileStatic
class EventOfferingAdapter implements Offering {

    Event event

    EventOfferingAdapter(Event event) {
        this.event = event
    }

    @Override
    String getCourse() {
        event.name
    }

    @Override
    String getDates() {
        event.date
    }

    @Override
    String getEnrollmentLink() {
        event.link
    }

    @Override
    String getInstructors() {
        null
    }

    @Override
    String getHours() {
        null
    }
}
