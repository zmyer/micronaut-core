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
package io.micronaut.docs.config.builder

import io.micronaut.context.annotation.ConfigurationBuilder
import io.micronaut.context.annotation.ConfigurationProperties

// tag::imports[]
import io.micronaut.context.annotation.ConfigurationBuilder
import io.micronaut.context.annotation.ConfigurationProperties
// end::imports[]

/**
 * @author James Kleeh
 * @since 1.0
 */
// tag::class[]
@ConfigurationProperties('my.engine') // <1>
class EngineConfig {

    @ConfigurationBuilder(prefixes = "with") // <2>
    EngineImpl.Builder builder = EngineImpl.builder()

    @ConfigurationBuilder(prefixes = "with", configurationPrefix = "crank-shaft") // <3>
    CrankShaft.Builder crankShaft = CrankShaft.builder()

    SparkPlug.Builder sparkPlug = SparkPlug.builder()

    @ConfigurationBuilder(prefixes = "with", configurationPrefix = "spark-plug")
    void setSparkPlug(SparkPlug.Builder sparkPlug) {
        this.sparkPlug = sparkPlug
    }
}
// end::class[]
