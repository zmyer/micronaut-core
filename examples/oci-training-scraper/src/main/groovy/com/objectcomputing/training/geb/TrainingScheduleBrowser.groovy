package com.objectcomputing.training.geb

import com.objectcomputing.training.model.Event

import com.objectcomputing.training.model.Offering
import com.objectcomputing.training.model.Track
import geb.Browser

class TrainingScheduleBrowser {

    Browser browser

    TrainingScheduleBrowser() {
        browser =  new Browser()
        browser.baseUrl = 'https://objectcomputing.com'
    }

    List<Event> eventList(List<String> keywords) {
        browser.to EventsPage
        EventsPage eventsPage = browser.page
        List<Event> eventList = eventsPage.eventList()
        eventList.findAll { Event event ->
            keywords.any { String keyword ->
                event.name.toLowerCase().contains(keyword.toLowerCase())
            }
        }
    }

    Set<Offering> offerings(Long trackId = null) {

        TrackSelectorPage selectorPage = browser.to TrackSelectorPage
        Set<Track> tracks = selectorPage.tracks().findAll { it.name != 'All Tracks' }

        Set<Offering> offerings = []
        for (Track track : tracks ) {
            if ( trackId != null && track.id != trackId) {
                continue
            }
            Set<Offering> trackOfferings = fetchTrackOfferings(browser, track)
            trackOfferings.each { Offering  offering ->
                populateOfferingSoldout(browser, track, offering)
            }
            offerings += trackOfferings
        }

        offerings
    }

    Set<Offering> fetchTrackOfferings(Browser browser, Track track) {
        TrainingSchedulePage page = browser.to TrainingSchedulePage, track.id
        Set<Offering> offerings = page.offerings()
        offerings.each { it.track = track }
        offerings
    }

    void populateOfferingSoldout(Browser browser, Track track, Offering offering) {
        TrainingScheduleModalPage page = browser.to TrainingScheduleModalPage, track.id, offering.id
        offering.soldOut = page.isSoldOut()
        offering.enrollmentLink = page.enrollmentUrl()
    }
}
