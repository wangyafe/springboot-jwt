package com.pjb.springbootjjwt.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Permission {
    // 这里可自行修改，添加一些变量
}

