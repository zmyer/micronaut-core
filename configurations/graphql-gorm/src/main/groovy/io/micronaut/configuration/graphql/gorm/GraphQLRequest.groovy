package io.micronaut.configuration.graphql.gorm

class GraphQLRequest {

    String query
    String operationName
    Map variables

    GraphQLRequest(String query, String operationName, Map variables) {
        this.query = query
        this.operationName = operationName
        this.variables = variables == null ? Collections.emptyMap() : variables
    }

    GraphQLRequest(Map data) {
        query = data.query.toString()
        operationName = data.containsKey('operationName') ? data.operationName : null
        variables = (data.variables instanceof Map) ? (Map)data.variables : Collections.emptyMap()
    }

    GraphQLRequest(String query) {
        this(query, null, null)
    }

}
