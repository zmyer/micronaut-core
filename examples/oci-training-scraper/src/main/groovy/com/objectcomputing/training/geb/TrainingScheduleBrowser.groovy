package com.objectcomputing.training.geb

import com.objectcomputing.training.model.Offering
import com.objectcomputing.training.model.Track
import geb.Browser

class TrainingScheduleBrowser {

    static Set<Offering> offerings(Long trackId = null) {
        Browser browser = new Browser()
        browser.baseUrl = 'https://objectcomputing.com'

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

    static Set<Offering> fetchTrackOfferings(Browser browser, Track track) {
        TrainingSchedulePage page = browser.to TrainingSchedulePage, track.id
        Set<Offering> offerings = page.offerings()
        offerings.each { it.track = track }
        offerings
    }

    static void populateOfferingSoldout(Browser browser, Track track, Offering offering) {
        TrainingScheduleModalPage page = browser.to TrainingScheduleModalPage, track.id, offering.id
        offering.soldOut = page.isSoldOut()
        offering.enrollmentLink = page.enrollmentUrl()
    }
}
