package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) //where to use Annotation
@Retention(RetentionPolicy.RUNTIME) //life circle
public @interface AutoFill {
    OperationType value();
}

