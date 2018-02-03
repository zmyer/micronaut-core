package com.objectcomputing.training

import com.objectcomputing.training.model.Offering
import org.particleframework.context.ApplicationContext
import org.particleframework.context.annotation.Value
import org.particleframework.http.HttpResponse
import org.particleframework.http.annotation.Controller
import org.particleframework.http.annotation.Get
import org.particleframework.http.annotation.Post
import org.particleframework.management.endpoint.refresh.RefreshEndpoint
import org.particleframework.runtime.context.scope.refresh.RefreshEvent

import javax.inject.Inject
import javax.inject.Singleton

import static org.particleframework.http.HttpResponse.notFound
import static org.particleframework.http.HttpResponse.ok

@Controller
@Singleton
class TrainingController {

    @Value("oci.training.refresh.enabled")
    Boolean refreshEnabled

    @Inject
    ApplicationContext applicationContext

    @Inject
    TrainingUseCase trainingUseCase

    @Get("/")
    HttpResponse<Set<Offering>> index() {
        ok(trainingUseCase.findAllOfferings())
    }

    @Get("/evict")
    HttpResponse<Map<String, String>> evict() {
        if (!refreshEnabled) {
            return notFound()
        }

        applicationContext.publishEvent(new RefreshEvent())

        return ok([msg: 'OK'])
    }

}