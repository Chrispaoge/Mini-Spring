package com.ycj.web.mvc;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD) //作用目标是Controller中的方法
public @interface RequestMapping {
    String value() ; //添加属性，用来保存需要映射的URI
}
