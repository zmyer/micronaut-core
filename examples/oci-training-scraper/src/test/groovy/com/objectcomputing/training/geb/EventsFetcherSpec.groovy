package com.objectcomputing.training.geb

import com.objectcomputing.training.model.Event
import geb.Browser
import spock.lang.Specification

class EventsFetcherSpec extends Specification {

    void 'fetch events'() {
        given:
        Browser browser = new Browser()
        browser.baseUrl = 'https://objectcomputing.com'

        when:
        browser.to EventsPage
        EventsPage page = browser.page
        List<Event> eventList = page.eventList()

        then:
        eventList
        eventList.each { Event event ->
            assert event.name != null
            assert event.link != null
            assert event.location != null
            assert event.date != null
        }
    }
}
