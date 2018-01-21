package com.objectcomputing.training

import com.objectcomputing.training.geb.TrainingScheduleBrowser
import com.objectcomputing.training.model.Event
import com.objectcomputing.training.model.EventOfferingAdapter
import com.objectcomputing.training.model.Offering
import groovy.transform.CompileStatic
import org.particleframework.runtime.context.scope.Refreshable
import javax.annotation.PostConstruct
import org.particleframework.context.annotation.Value

@CompileStatic
@Refresable
class TrainingOfferingsService {

    Set<Offering> offerings = new HashSet<Offering>()

    @Value('oci.grails.keywords')
    List<String> keywords

    @PostConstruct
    void setup() {
        refresh()
    }

    void refresh() {
        TrainingScheduleBrowser browser = new TrainingScheduleBrowser()
        offerings = browser.offerings(11l)
        List<Event> eventList = browser.eventList(keywords)
        addEventsNotAlreadyInOffering(eventList)
    }

    void addEventsNotAlreadyInOffering(List<Event> eventList) {
        eventList.each { Event event ->
            boolean present = offerings.find { Offering offering -> offering.enrollmentLink == event.link }
            if ( !present ) {
                offerings << new EventOfferingAdapter(event)
            }
        }
    }

    Set<Offering> getOfferings() {
        return offerings
    }

    void set(Set<Offering> offerings) {
        this.offerings = offerings
    }

    Set<Offering> findAllByTrack(Long trackId) {
        offerings.findAll { Offering offering -> offering.track.id == trackId } as Set<Offering>
    }

    Set<String> findAllTracks() {
        offerings.collect { Offering offering -> offering.track.name }
    }

}
