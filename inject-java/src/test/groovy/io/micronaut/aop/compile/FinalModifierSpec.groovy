package io.micronaut.aop.compile

import io.micronaut.inject.AbstractTypeElementSpec

class FinalModifierSpec extends AbstractTypeElementSpec {


    void "test final modifier on class with AOP advice doesn't compile"() {
        when:
        buildBeanDefinition('test.$MyBeanDefinition$Intercepted', '''
package test;

import io.micronaut.aop.simple.*;
import io.micronaut.context.annotation.*;

@Mutating("someVal")
@javax.inject.Singleton
final class MyBean {

    private String myValue;
    
    MyBean(@Value("${foo.bar}") String val) {
        this.myValue = val;
    }
    
    public String someMethod() {
        return myValue;
    }

}
''')
        then:
        def e = thrown(RuntimeException)
        e.message.contains 'error: Cannot apply AOP advice to final class. Class must be made non-final to support proxying: test.MyBean'
    }

    void "test final modifier on method with AOP advice doesn't compile"() {
        when:
        buildBeanDefinition('test.$MyBeanDefinition$Intercepted', '''
package test;

import io.micronaut.aop.simple.*;
import io.micronaut.context.annotation.*;

@Mutating("someVal")
@javax.inject.Singleton
class MyBean {

    private String myValue;
    
    MyBean(@Value("${foo.bar}") String val) {
        this.myValue = val;
    }
    
    public final String someMethod() {
        return myValue;
    }

}
''')
        then:
        def e = thrown(RuntimeException)
        e.message.contains 'error: Public method defines AOP advice but is declared final. Either make the method non-public or apply AOP advice only to public methods declared on class.'
    }
}
