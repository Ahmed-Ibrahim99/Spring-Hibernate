package com.example.Hibernate.annotation;

import com.example.Hibernate.utils.FieldTypes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldAnnotation {
    String name() default "";
    int type() default FieldTypes.stringType;
    boolean isPrimary() default false;
}
