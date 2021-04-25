package com.demo.log02;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME) // 这个非常重要，代表注解的保留期
public @interface Log {
}
