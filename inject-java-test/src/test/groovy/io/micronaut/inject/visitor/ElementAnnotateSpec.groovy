package io.micronaut.inject.visitor

import io.micronaut.annotation.processing.TypeElementVisitorProcessor
import io.micronaut.annotation.processing.test.AbstractTypeElementSpec
import io.micronaut.annotation.processing.test.JavaParser
import io.micronaut.aop.Introduction
import io.micronaut.core.annotation.AnnotationValueBuilder
import io.micronaut.core.annotation.Introspected
import io.micronaut.core.beans.BeanIntrospection
import io.micronaut.inject.BeanDefinition
import io.micronaut.inject.ast.ClassElement
import io.micronaut.inject.ast.MethodElement
import io.micronaut.inject.ast.PropertyElement
import io.micronaut.inject.beans.visitor.IntrospectedTypeElementVisitor
import io.micronaut.inject.writer.BeanDefinitionVisitor

import javax.annotation.processing.SupportedAnnotationTypes
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

class ElementAnnotateSpec extends AbstractTypeElementSpec {

    void "test annotate introduction advice"() {
        when:
        BeanDefinition beanDefinition = buildBeanDefinition('test.MyInterface' + BeanDefinitionVisitor.PROXY_SUFFIX, '''
package test;

import io.micronaut.inject.visitor.Stub;
import io.micronaut.context.annotation.*;
import java.net.*;
import javax.validation.constraints.*;

@Stub
interface MyInterface{
    void save(@NotBlank String name, @Min(1L) int age);
    void saveTwo(String name);
}

''')
        then:
        !beanDefinition.isAbstract()
        beanDefinition != null
        beanDefinition.getRequiredMethod("save", String, int).hasAnnotation(Ann)
        beanDefinition.getRequiredMethod("saveTwo", String).hasAnnotation(Ann)
    }

    void "test that elements can be dynamically annotated at compilation time"() {
        given:
        def definition = buildBeanDefinition('test.TestListener', '''
package test;
import io.micronaut.context.annotation.*;
import javax.inject.Singleton;

@Singleton
class TestListener {

    @Executable
    void receive(String v) {
    }
    
    @Executable
    int[] receiveArray(int[] v) {
        return v;
    }
    
    @Executable
    int receiveInt(int v) {
        return v;
    }
}

''')

        expect:
        definition.hasAnnotation(Ann)
        definition.getValue(Ann, "foo", String).get() == 'bar'
        definition.findMethod("receive", String).get().hasAnnotation(Ann)
    }

    void "test annotation bean introspection properties"() {
        given:
        def introspection = buildBeanIntrospection('test.Test', '''
package test;

import io.micronaut.core.annotation.Introspected;

@Introspected
class Test {
    private String name;
    
    public String getName() { 
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
''')

        expect:
        introspection.getRequiredProperty("name", String).stringValue("foo.bar.Ann", 'foo')
            .get() == 'bar'
    }

    @Override
    protected JavaParser newJavaParser() {
        return new JavaParser() {
            @Override
            protected TypeElementVisitorProcessor getTypeElementVisitorProcessor() {
                return new MyTypeElementVisitorProcessor()
            }
        }
    }

    @SupportedAnnotationTypes("*")
    static class MyTypeElementVisitorProcessor extends TypeElementVisitorProcessor {
        @Override
        protected Collection<TypeElementVisitor> findTypeElementVisitors() {
            return [new MyAnnotatingTypeElementVisitor(), new IntrospectedTypeElementVisitor()]
        }
    }

    static class MyAnnotatingTypeElementVisitor implements TypeElementVisitor {

        @Override
        int getOrder() {
            return 100
        }

        @Override
        void visitClass(ClassElement element, VisitorContext context) {
            if (!element.hasStereotype(Introduction)) {
                element.annotate(Ann) { AnnotationValueBuilder builder ->
                    builder.member("foo", "bar")
                }
            }

            if (element.hasStereotype(Introspected)) {
                List<PropertyElement> props = element.getBeanProperties()
                for (PropertyElement pe : props) {
                    pe.annotate("foo.bar.Ann") { AnnotationValueBuilder builder ->
                        builder.member("foo", "bar")
                    }
                }
            }
        }

        @Override
        void visitMethod(MethodElement element, VisitorContext context) {
            element.annotate(Ann) { AnnotationValueBuilder builder ->
                builder.member("foo", "bar")
            }
        }
    }
}

@Retention(RetentionPolicy.SOURCE)
@interface Ann {
    String foo() default ""
}