package com.objectcomputing.training

import com.objectcomputing.training.geb.TrainingScheduleBrowser
import com.objectcomputing.training.model.Event

import com.objectcomputing.training.model.Offering
import groovy.transform.CompileStatic
import org.particleframework.runtime.context.scope.Refreshable
import javax.annotation.PostConstruct
import org.particleframework.context.annotation.Value

@CompileStatic
@Refreshable
class TrainingOfferingsService implements TrainingUseCase {

    Set<Offering> offerings = new HashSet<Offering>()

    @Value('oci.grails.keywords')
    String keywords

    @Value('oci.training.track')
    Long trackId

    @PostConstruct
    void setup() {
        refresh()
    }

    void refresh() {
        TrainingScheduleBrowser browser = new TrainingScheduleBrowser()
        offerings = browser.offerings(trackId)
        List<Event> eventList = browser.eventList(keywords.split(',') as List<String>)
        addEventsNotAlreadyInOffering(eventList)
    }

    void addEventsNotAlreadyInOffering(List<Event> eventList) {
        for ( Event event : eventList ) {
            boolean existsOfferingWithSameLink = offerings.find { Offering offering -> offering.enrollmentLink == event.link }
            boolean existsOfferingWithSameName = offerings.find { Offering offering ->
                cleanup(event.name).contains(cleanup(offering.course))
            }

            if ( existsOfferingWithSameLink || existsOfferingWithSameName ) {
                continue
            }
            offerings << Offering.of(event)
        }
    }

    String cleanup(String name) {
        name.toLowerCase()
                .trim()
                .replaceAll(' and ', '')
                .replaceAll(' & ', '')
    }


    @Override
    Set<Offering> findAllOfferings() {
        offerings
    }
}
