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

package io.micronaut.annotation.processing.visitor;

import io.micronaut.core.annotation.AnnotationMetadata;
import io.micronaut.core.annotation.Internal;
import io.micronaut.inject.ast.ClassElement;
import io.micronaut.inject.ast.ParameterElement;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * Implementation of the {@link ParameterElement} interface for Java.
 *
 * @author graemerocher
 * @since 1.0
 */
@Internal
class JavaParameterElement extends AbstractJavaElement implements ParameterElement {

    private final JavaVisitorContext visitorContext;

    /**
     * Default constructor.
     *
     * @param element The variable element
     * @param annotationMetadata The annotation metadata
     * @param visitorContext The visitor context
     */
    JavaParameterElement(VariableElement element, AnnotationMetadata annotationMetadata, JavaVisitorContext visitorContext) {
        super(element, annotationMetadata);
        this.visitorContext = visitorContext;
    }

    @Override
    public ClassElement getType() {
        TypeMirror returnType = getNativeType().asType();
        return mirrorToClassElement(returnType, visitorContext);
    }

    @Override
    public VariableElement getNativeType() {
        return (VariableElement) super.getNativeType();
    }
}
