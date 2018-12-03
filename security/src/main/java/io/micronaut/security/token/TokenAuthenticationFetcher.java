/*
 * Copyright 2017-2018 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.micronaut.security.token;

import io.micronaut.context.event.ApplicationEventPublisher;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.event.TokenValidatedEvent;
import io.micronaut.security.filters.AuthenticationFetcher;
import io.micronaut.security.token.reader.DefaultTokenResolver;
import io.micronaut.security.token.reader.TokenReader;
import io.micronaut.security.token.reader.TokenResolver;
import io.micronaut.security.token.validator.TokenValidator;
import io.reactivex.Flowable;
import org.reactivestreams.Publisher;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

import static io.micronaut.security.filters.SecurityFilter.TOKEN;

/**
 * Attempts to retrieve a token form the {@link HttpRequest} and if existing validated.
 * It uses a {@link TokenResolver} and the list of {@link TokenValidator} registered in the ApplicationContext.
 *
 * @author Sergio del Amo
 * @author Graeme Rocher
 * @since 1.0
 */
@Singleton
public class TokenAuthenticationFetcher implements AuthenticationFetcher {

    /**
     * The order of the fetcher.
     */
    public static final Integer ORDER = 0;

    protected final Collection<TokenValidator> tokenValidators;
    private final TokenResolver tokenResolver;
    protected final ApplicationEventPublisher eventPublisher;

    /**
     * @param tokenValidators The list of {@link TokenValidator} which attempt to validate the request
     * @param tokenResolver   The {@link io.micronaut.security.token.reader.TokenResolver} which returns the first found token in the request.
     * @param eventPublisher  The Application event publisher
     */
    @Inject
    public TokenAuthenticationFetcher(Collection<TokenValidator> tokenValidators,
                                      TokenResolver tokenResolver,
                                      ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
        this.tokenResolver = tokenResolver;
        this.tokenValidators = tokenValidators;
    }

    /**
     * @deprecated Use {@link #TokenAuthenticationFetcher(Collection, TokenResolver, ApplicationEventPublisher)} instead.
     * A {@link TokenResolver} can be created using {@link DefaultTokenResolver#DefaultTokenResolver(Collection)}.
     *
     * @param tokenValidators The list of {@link TokenValidator} which attempt to validate the request
     * @param tokenReaders    The list {@link TokenReader} which attempt to read the request
     * @param eventPublisher  The Application event publisher
     */
    @Deprecated
    public TokenAuthenticationFetcher(Collection<TokenValidator> tokenValidators,
                                      Collection<TokenReader> tokenReaders,
                                      ApplicationEventPublisher eventPublisher) {
        this(tokenValidators, new DefaultTokenResolver(tokenReaders), eventPublisher);
    }


    @Override
    public Publisher<Authentication> fetchAuthentication(HttpRequest<?> request) {

        Optional<String> token = tokenResolver.resolveToken(request);

        if (!token.isPresent()) {
            return Flowable.empty();
        }

        Iterator<TokenValidator> tokenValidatorIterator = tokenValidators.iterator();
        String tokenString = token.get();
        return attemptTokenValidation(request, tokenValidatorIterator, tokenString);
    }

    @Override
    public int getOrder() {
        return ORDER;
    }

    private Flowable<Authentication> attemptTokenValidation(HttpRequest<?> request, Iterator<TokenValidator> tokenValidatorIterator, String tokenString) {
        if (tokenValidatorIterator.hasNext()) {
            TokenValidator tokenValidator = tokenValidatorIterator.next();
            return Flowable.just(tokenString).switchMap(tokenValue ->
                Flowable.fromPublisher(tokenValidator.validateToken(tokenValue)).map(authentication -> {
                    request.setAttribute(TOKEN, tokenValue);
                    eventPublisher.publishEvent(new TokenValidatedEvent(tokenValue));
                    return authentication;
                })
            ).switchIfEmpty(attemptTokenValidation(
                    request, tokenValidatorIterator, tokenString
            ));
        }
        return Flowable.empty();
    }
}
