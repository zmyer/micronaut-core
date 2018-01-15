package com.objectcomputing.training.geb

import geb.Page

class TrainingScheduleModalPage extends Page {

    static url = '/training/schedule'

    static content = {
        modalWindow(required: false) { $('.ws-modal-dialog', 0) }
        enrollLink(required: false) { $('a', text: iContains('Enroll Now'), 0) }
    }

    @Override
    String convertToPath(Object[] args) {
        if ( args.size() > 1 ) {
            return "?track=${args[0]}#schedule-offering-${args[1]}"
        }
    }

    String enrollmentUrl() {
        if ( !enrollLink.empty ) {
            return enrollLink.getAttribute('href')
        }
        null
    }

    boolean isSoldOut() {
        if ( !modalWindow.empty ) {
            return modalWindow.text().contains('Sold Out')
        }
        false
    }
}
