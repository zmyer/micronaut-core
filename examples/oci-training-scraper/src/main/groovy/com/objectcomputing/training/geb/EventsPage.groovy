package com.objectcomputing.training.geb

import com.objectcomputing.training.model.Event
import geb.Page

class EventsPage extends Page {

    static url = "/resources/events/"

    static content = {
        events { $("table.table tbody tr").moduleList(EventModule) }
    }

    List<Event> eventList() {
        events.collect { EventModule eventModule -> eventModule as Event }
    }
}