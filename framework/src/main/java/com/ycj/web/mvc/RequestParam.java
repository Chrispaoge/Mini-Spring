package com.ycj.web.mvc;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)//RequestParam注解使用在Controller中方法的参数上
public @interface RequestParam {
    String value() ; //需要传入一个参数，表示它要接收的requeryString的key
}
