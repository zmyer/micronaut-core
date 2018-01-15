package com.objectcomputing.training

import com.objectcomputing.training.geb.TrainingScheduleBrowser
import com.objectcomputing.training.model.Offering
import org.particleframework.runtime.context.scope.Refreshable

import javax.annotation.PostConstruct
import javax.inject.Singleton

@Singleton
class TrainingOfferingsService {

    Set<Offering> offerings = new HashSet<Offering>()

    @PostConstruct
    void setup() {
        refresh()
    }

    void refresh() {
        offerings = TrainingScheduleBrowser.offerings(11l)
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
