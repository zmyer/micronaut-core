package co.curated.particleclient

import co.curated.CuratedApiConstants
import co.curated.CuratedApiHeaders
import com.google.common.net.HttpHeaders
import groovy.transform.CompileStatic
import org.particleframework.context.annotation.Value
import org.particleframework.http.HttpResponse
import org.particleframework.http.MutableHttpRequest
import org.particleframework.http.annotation.Filter
import org.particleframework.http.filter.ClientFilterChain
import org.particleframework.http.filter.HttpClientFilter
import org.reactivestreams.Publisher

@CompileStatic
@Filter("/*/api/v1/**")
class CuratedApiFilter implements HttpClientFilter, CuratedApiHeaders {

    @Value('curated.apiKey')
    String apiKey

    @Override
    Publisher<? extends HttpResponse<?>> doFilter(MutableHttpRequest<?> request, ClientFilterChain chain) {
        request.header(HttpHeaders.AUTHORIZATION, header('Authorization'))
        request.header(HttpHeaders.ACCEPT, header('Accept'))
        request.header(HttpHeaders.CONTENT_TYPE, header('Content-Type'))
        chain.proceed(request)
    }
}
