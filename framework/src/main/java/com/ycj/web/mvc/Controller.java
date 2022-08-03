package com.ycj.web.mvc;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME) //保留到运行期
@Target(ElementType.TYPE) //作用目标是类
public @interface Controller {
}
