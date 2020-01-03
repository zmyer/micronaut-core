/*
 * Copyright 2017-2019 original authors
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
package io.micronaut.http.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import io.micronaut.context.annotation.AliasFor;
import io.micronaut.core.bind.annotation.Bindable;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * An annotation that can be applied to method argument to indicate that the method argument is bound from a specific
 * part of a "multipart/form-data" request.
 *
 * @author Graeme Rocher
 * @since 1.0
 */
@Documented
@Retention(RUNTIME)
@Target({ElementType.PARAMETER})
@Bindable
public @interface Part {

    /**
     * @return The name of the part, otherwise it is inferred from the parameter name
     */
    @AliasFor(annotation = Bindable.class, member = "value")
    String value() default "";
}