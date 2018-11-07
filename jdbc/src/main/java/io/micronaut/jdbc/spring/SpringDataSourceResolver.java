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

package io.micronaut.jdbc.spring;

import io.micronaut.core.annotation.Internal;
import io.micronaut.jdbc.DataSourceResolver;
import org.springframework.jdbc.datasource.DelegatingDataSource;

import javax.inject.Singleton;
import javax.sql.DataSource;

/**
 * Unwraps spring data source proxies.
 *
 * @author graemerocher
 * @since 1.0
 */
@Singleton
@Internal
public final class SpringDataSourceResolver implements DataSourceResolver {

    @Override
    public DataSource resolve(DataSource dataSource) {
        while (dataSource instanceof DelegatingDataSource) {
            dataSource = ((DelegatingDataSource) dataSource).getTargetDataSource();
        }
        return dataSource;
    }
}
