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

package io.micronaut.ast.groovy.visitor;

import groovy.lang.GroovyClassLoader;
import io.micronaut.ast.groovy.utils.AstAnnotationUtils;
import io.micronaut.core.convert.ArgumentConversionContext;
import io.micronaut.core.convert.value.MutableConvertibleValues;
import io.micronaut.core.convert.value.MutableConvertibleValuesMap;
import io.micronaut.core.reflect.ClassUtils;
import io.micronaut.inject.ast.ClassElement;
import io.micronaut.inject.ast.Element;
import io.micronaut.inject.visitor.VisitorContext;
import io.micronaut.inject.writer.DirectoryClassWriterOutputVisitor;
import io.micronaut.inject.writer.GeneratedFile;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.control.ErrorCollector;
import org.codehaus.groovy.control.Janitor;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.control.messages.Message;
import org.codehaus.groovy.control.messages.SimpleMessage;
import org.codehaus.groovy.control.messages.SyntaxErrorMessage;
import org.codehaus.groovy.syntax.SyntaxException;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * The visitor context when visiting Groovy code.
 *
 * @author James Kleeh
 * @author Graeme Rocher
 * @since 1.0
 */
public class GroovyVisitorContext implements VisitorContext {
    private static final String ATTR_VISITOR_ATTRIBUTES = "micronaut.visitor.attributes";
    private final ErrorCollector errorCollector;
    private final SourceUnit sourceUnit;
    private final MutableConvertibleValues<Object> attributes;

    /**
     * @param sourceUnit The {@link SourceUnit}
     */
    public GroovyVisitorContext(SourceUnit sourceUnit) {
        this.sourceUnit = sourceUnit;
        this.errorCollector = sourceUnit.getErrorCollector();
        final ModuleNode ast = sourceUnit.getAST();
        final boolean hasModule = ast != null;
        final Object attrs = hasModule ? ast.getNodeMetaData(ATTR_VISITOR_ATTRIBUTES) : null;
        if (attrs instanceof MutableConvertibleValues) {
            this.attributes = (MutableConvertibleValues<Object>) attrs;
        } else {
            this.attributes = new MutableConvertibleValuesMap<>();
            if (hasModule) {
                ast.putNodeMetaData(ATTR_VISITOR_ATTRIBUTES, this.attributes);
            }
        }
    }

    @Override
    public Optional<ClassElement> getClassElement(String name) {
        if (name == null) {
            return Optional.empty();
        }
        List<ClassNode> classes = sourceUnit.getAST().getClasses();
        for (ClassNode aClass : classes) {
            if (name.equals(aClass.getName())) {
                return Optional.of(new GroovyClassElement(sourceUnit, aClass, AstAnnotationUtils.getAnnotationMetadata(sourceUnit, aClass)));
            }
        }

        GroovyClassLoader classLoader = sourceUnit.getClassLoader();
        if (classLoader != null) {
            return ClassUtils.forName(name, classLoader).map(aClass -> {
                ClassNode cn = ClassHelper.make(aClass);
                return new GroovyClassElement(sourceUnit, cn, AstAnnotationUtils.getAnnotationMetadata(sourceUnit, cn));
            });
        }
        return Optional.empty();
    }

    @Override
    public void info(String message, @Nullable Element element) {
        StringBuilder msg = new StringBuilder("Note: ").append(message);
        if (element != null) {
            ASTNode expr = (ASTNode) element.getNativeType();
            final String sample = sourceUnit.getSample(expr.getLineNumber(), expr.getColumnNumber(), new Janitor());
            msg.append("\n\n").append(sample);
        }
        System.out.println(msg.toString());
    }

    @Override
    public void info(String message) {
        System.out.println("Note: " + message);
    }

    @Override
    public void fail(String message, @Nullable Element element) {
        Message msg;
        if (element != null) {
            msg = buildErrorMessage(message, element);
        } else {
            msg = new SimpleMessage(message, sourceUnit);
        }
        errorCollector.addError(msg);
    }

    @Override
    public void warn(String message, @Nullable Element element) {
        StringBuilder msg = new StringBuilder("WARNING: ").append(message);
        if (element != null) {
            ASTNode expr = (ASTNode) element.getNativeType();
            final String sample = sourceUnit.getSample(expr.getLineNumber(), expr.getColumnNumber(), new Janitor());
            msg.append("\n\n").append(sample);
        }
        System.out.println(msg.toString());

    }

    @Override
    public Optional<GeneratedFile> visitMetaInfFile(String path) {
        File classesDir = sourceUnit.getConfiguration().getTargetDirectory();
        if (classesDir != null) {

            DirectoryClassWriterOutputVisitor outputVisitor = new DirectoryClassWriterOutputVisitor(
                    classesDir
            );
            return outputVisitor.visitMetaInfFile(path);
        }

        return Optional.empty();
    }

    @Override
    public Optional<GeneratedFile> visitGeneratedFile(String path) {
        File classesDir = sourceUnit.getConfiguration().getTargetDirectory();
        if (classesDir != null) {

            DirectoryClassWriterOutputVisitor outputVisitor = new DirectoryClassWriterOutputVisitor(
                    classesDir
            );
            return outputVisitor.visitGeneratedFile(path);
        }

        return Optional.empty();
    }

    /**
     * @return The source unit
     */
    SourceUnit getSourceUnit() {
        return sourceUnit;
    }

    private SyntaxErrorMessage buildErrorMessage(String message, Element element) {
        ASTNode expr = (ASTNode) element.getNativeType();
        return new SyntaxErrorMessage(
            new SyntaxException(message + '\n', expr.getLineNumber(), expr.getColumnNumber(),
                expr.getLastLineNumber(), expr.getLastColumnNumber()), sourceUnit);
    }

    @Override
    public MutableConvertibleValues<Object> put(CharSequence key, @Nullable Object value) {
        return attributes.put(key, value);
    }

    @Override
    public MutableConvertibleValues<Object> remove(CharSequence key) {
        return attributes.remove(key);
    }

    @Override
    public MutableConvertibleValues<Object> clear() {
        return attributes.clear();
    }

    @Override
    public Set<String> names() {
        return attributes.names();
    }

    @Override
    public Collection<Object> values() {
        return attributes.values();
    }

    @Override
    public <T> Optional<T> get(CharSequence name, ArgumentConversionContext<T> conversionContext) {
        return attributes.get(name, conversionContext);
    }
}
