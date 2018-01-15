package com.objectcomputing.training

import com.objectcomputing.training.model.Offering
import org.particleframework.context.annotation.Value
import org.particleframework.http.HttpResponse
import org.particleframework.http.annotation.Controller
import org.particleframework.web.router.annotation.Get
import org.particleframework.web.router.annotation.Post

import javax.inject.Inject
import javax.inject.Singleton

import static org.particleframework.http.HttpResponse.notFound
import static org.particleframework.http.HttpResponse.ok

@Controller
@Singleton
public class TrainingController {

    @Value("com.objectcomputing.training.refresh.enabled")
    Boolean refreshEnabled

    @Inject
    TrainingOfferingsService trainingOfferingsService

    @Get("/")
    HttpResponse<Set<Offering>> index(Optional<Long> trackId) {
        if (!refreshEnabled) {
            return notFound()
        }
        return ok(trackId.isPresent() ? trainingOfferingsService.findAllByTrack(trackId.get()) : trainingOfferingsService.getOfferings())
    }

    @Get("/tracks")
    HttpResponse<Map<String, Set<String>>> tracks() {
        if (!refreshEnabled) {
            return notFound()
        }

        return ok([tracks: trainingOfferingsService.findAllTracks()])
    }

    @Post("/evict")
    HttpResponse<Map<String, String>> evict() {
        if (!refreshEnabled) {
            return notFound()
        }

        trainingOfferingsService.refresh()

        return ok([msg: 'OK'])
    }

}