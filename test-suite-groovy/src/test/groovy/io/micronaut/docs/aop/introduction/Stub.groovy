package io.micronaut.docs.aop.introduction

// tag::imports[]

import io.micronaut.aop.Introduction
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Type

import java.lang.annotation.Documented
import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.Target

import static java.lang.annotation.RetentionPolicy.RUNTIME
// end::imports[]

// tag::class[]
@Introduction // <1>
@Type(StubIntroduction.class) // <2>
@Bean // <3>
@Documented
@Retention(RUNTIME)
@Target([ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.METHOD])
@interface Stub {
    String value() default ""
}
// end::class[]