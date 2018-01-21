package com.objectcomputing.training.geb

import com.objectcomputing.training.model.Event
import com.objectcomputing.training.model.Offering
import com.objectcomputing.training.model.Track
import geb.Module

class EventModule extends Module {

    static content = {
        cell { $('td', it) }
        eventLink { cell(0).$('a', 0).getAttribute('href') }
        eventName { cell(0).text() }
        dateString { cell(1).text() }
        location { cell(2).text() }
    }

    Object asType(Class clazz) {
        if (clazz == Event) {
            return new Event(name: eventName,
                    date: dateString,
                    link: eventLink,
                    location: location
            )
        }
        else {
            super.asType(clazz)
        }
    }
}
