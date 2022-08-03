package com.ycj.beans;

import java.lang.annotation.*;

//从注解使用在Bean的属性上，添加了这个注解的属性就需要添加对应的依赖
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AutoWired {
}
