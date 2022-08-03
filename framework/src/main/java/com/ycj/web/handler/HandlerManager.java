package com.ycj.web.handler;

import com.ycj.web.mvc.Controller;
import com.ycj.web.mvc.RequestMapping;
import com.ycj.web.mvc.RequestParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

//从所有类中找出带有@Controller注解的类，然后将其中的带有@RequestMapping注解的方法包装成一个MappingHandler对象，放入MappingHandler容器中
public class HandlerManager {
    // 容器。用来保存多个Handler
    public static List<MappingHandler> mappingHandlerList = new ArrayList<>() ;

    //将classList中包含有@Controller注解的类挑选出
    //然后将其中的MappingHandler方法（即带有@RequestMapping注解的方法）初始化成MappingHandler
    public static void resolveMappingHandler(List<Class<?>> classList){
        for (Class<?> cls : classList) {
            if (cls.isAnnotationPresent(Controller.class)){
                parseHandlerFromController(cls); //此类存在@Controller注解，解析这个类
            }
        }
    }

    //解析带有@Controller注解的类。
    //具体做法：在这些类中，找到带有@RequestMapping注解的方法。
    //每一个带有@RequestMapping注解的方法都对应一个MappingHandler
    private static void parseHandlerFromController(Class<?> cls) {
        Method[] methods = cls.getDeclaredMethods();//通过反射，获取到这个类的所有方法
        for (Method method : methods) {  //遍历这些方法，找到带有@RequestMapping注解的方法
            if (!method.isAnnotationPresent(RequestMapping.class)){
                continue;
            }
            //从注解的属性中获取uri。如写在SalaryController中的@RequestMapping("/get_salary.json")中的/get_salary.json
            String uri = method.getDeclaredAnnotation(RequestMapping.class).value() ;
            List<String> paramNameList = new ArrayList<>() ; //需要的参数
            for (Parameter parameter : method.getParameters()) { //遍历所有的参数，找到那些添加了@RequestParam注解的参数
                if (parameter.isAnnotationPresent(RequestParam.class)){
                    //从注解中获取参数的名字。如@RequestParam("experience")中的experience
                    paramNameList.add(parameter.getDeclaredAnnotation(RequestParam.class).value()) ;
                }
            }

            //将参数名容器转换为数组的形式
            String[] params = paramNameList.toArray(new String[paramNameList.size()]);

            //准备工作做完，为当前的带有@RequestMapping注解的方法生成对应的MappingHandler对象
            MappingHandler mappingHandler = new MappingHandler(uri, method, cls, params) ;

            //将构造好的mappingHandler，放入容器中
            HandlerManager.mappingHandlerList.add(mappingHandler) ;
        }
    }
}
