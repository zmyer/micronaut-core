package io.micronaut.configuration.graphql.gorm

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import graphql.ExecutionInput
import graphql.ExecutionResult
import graphql.GraphQL
import io.micronaut.configuration.graphql.gorm.context.GraphQLContextBuilder
import io.micronaut.core.io.ResourceLoader
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.Post
import io.micronaut.http.server.types.files.StreamedFileCustomizableResponseType

import javax.annotation.Nullable
import javax.inject.Inject

@Controller("/graphql")
class GraphqlController {

    @Inject GraphqlConfiguration configuration
    @Inject ObjectMapper objectMapper
    @Inject GraphQLContextBuilder graphQLContextBuilder
    @Inject GraphQL graphQL

    private static final String graphiql = "classpath:graphiql.html"
    private static URL cachedBrowser = null

    private Map parseJson(String json) {
        def typeRef = new TypeReference<HashMap<String,Object>>() {}
        objectMapper.readValue(json, typeRef)
    }

    private HttpResponse executeRequest(GraphQLRequest graphQLRequest, HttpRequest request) {
        Object context = graphQLContextBuilder.buildContext(request)

        Map<String, Object> result = new LinkedHashMap<>()

        ExecutionResult executionResult = graphQL.execute(ExecutionInput.newExecutionInput()
                .query(graphQLRequest.query)
                .operationName(graphQLRequest.operationName)
                .context(context)
                .root(context)
                .variables(graphQLRequest.variables)
                .build())

        if (executionResult.errors.size() > 0) {
            result.put('errors', executionResult.errors)
        }
        result.put('data', executionResult.data)

        HttpResponse.ok(result)
    }

    @Get("/")
    HttpResponse get(String query, @Nullable String operationName, @Nullable String variables, HttpRequest request) {
        if (!configuration.enabled) {
            return HttpResponse.notFound()
        }
        Map variableMap = parseJson(variables)
        executeRequest(new GraphQLRequest(query, operationName, variableMap), request)
    }

    @Post("/")
    HttpResponse post(@Header String contentType, @Body String body, HttpRequest request) {
        if (!configuration.enabled) {
            return HttpResponse.notFound()
        }
        if (contentType == MediaType.APPLICATION_JSON) {
            Map data = parseJson(body)
            if (data.containsKey('query')) {
                return executeRequest(new GraphQLRequest(data), request)
            }
        } else if (contentType == "application/graphql") {
            return executeRequest(new GraphQLRequest(body), request)
        }

        HttpResponse.unprocessableEntity()
    }

    @Get("/browser")
    HttpResponse browser() {
        if (configuration.enabled && configuration.browser) {
            if (cachedBrowser == null) {
                Optional<ResourceLoader> resourceLoader = ResourceLoader.forResource(graphiql, this.getClass().getClassLoader())
                if (resourceLoader.isPresent()) {
                    Optional<URL> url = resourceLoader.get().getResource(graphiql)
                    if (url.isPresent()) {
                        cachedBrowser = url.get()
                    }
                }
            }

            if (cachedBrowser != null) {
                return HttpResponse.ok(new StreamedFileCustomizableResponseType(cachedBrowser))
            }
        }

        HttpResponse.notFound()
    }

}
