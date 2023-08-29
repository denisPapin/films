package com.dp.homework.films.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface MySecuredAnnotation {
    String[] value();
}
