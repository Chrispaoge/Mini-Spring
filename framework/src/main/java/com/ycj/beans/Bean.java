package com.ycj.beans;

import java.lang.annotation.*;

//用来标记一个类可以解析为Bean
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) //Bean注解是添加在类上的，所以这里为ElementType.TYPE
public @interface Bean {
}
