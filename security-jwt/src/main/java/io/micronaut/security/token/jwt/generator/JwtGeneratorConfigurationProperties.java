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

package io.micronaut.security.token.jwt.generator;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.security.token.jwt.config.JwtConfigurationProperties;

/**
 * {@link JwtGeneratorConfiguration} implementation.
 *
 * @author Sergio del Amo
 * @since 1.0
 */
@ConfigurationProperties(JwtGeneratorConfigurationProperties.PREFIX)
public class JwtGeneratorConfigurationProperties implements JwtGeneratorConfiguration {

    public static final String PREFIX = JwtConfigurationProperties.PREFIX + ".generator";

    /**
     * The default expiration.
     */
    @SuppressWarnings("WeakerAccess")
    public static final Integer DEFAULT_EXPIRATION = 3600;

    private Integer refreshTokenExpiration = null;
    private Integer accessTokenExpiration = DEFAULT_EXPIRATION;

    @Override
    public Integer getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }

    /**
     * If not specified, defaults to {@link #DEFAULT_EXPIRATION}.
     */
    @Override
    public Integer getAccessTokenExpiration() {
        return accessTokenExpiration;
    }

    /**
     * Refresh token expiration. By default refresh tokens, do not expire.
     * @param refreshTokenExpiration The expiration
     */
    public void setRefreshTokenExpiration(Integer refreshTokenExpiration) {
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    /**
     * Access token expiration. Default value ({@value #DEFAULT_EXPIRATION}).
     * @param accessTokenExpiration The expiration
     */
    public void setAccessTokenExpiration(Integer accessTokenExpiration) {
        if (accessTokenExpiration != null) {
            this.accessTokenExpiration = accessTokenExpiration;
        }
    }
}
