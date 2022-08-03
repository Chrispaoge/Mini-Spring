package com.ycj.web.handler;

import com.ycj.beans.BeanFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

//每一个MappingHandler都是一个请求映射器。Spring将所有的Servlet 都简化成MappingHandler
public class MappingHandler {
    private String uri ; // 保存待处理的URI
    private Method method ; //对应Controller方法
    private Class<?> controller ; //Method类是反射包中的类，要想调用Method还需要知道对应的类。此类也就是Controller类
    private String[] args ; //调用方法所需要的参数

    public MappingHandler(String uri, Method method, Class<?> controller, String[] args) {
        this.uri = uri;
        this.method = method;
        this.controller = controller;
        this.args = args;
    }

    //需要有一个handle方法，即使用MappingHandler对象处理成功后，返回结果为true
    public boolean handle(ServletRequest request, ServletResponse response) throws
            InstantiationException, IllegalAccessException, InvocationTargetException, IOException {
        //首先判断这个handler能否处理这个URI（通过ServletRequest获取URI）
        String requestURI = ((HttpServletRequest) request).getRequestURI();
        if (!this.uri.equals(requestURI)){ //判断存储的uri是否和传入的uri相等
            return false ;
        }
        //uri相等，调用handler中的method方法
        //准备参数
        Object[] parameters = new Object[args.length] ;
        for (int i = 0 ; i < args.length ; i ++){
            parameters[i] = request.getParameter(args[i]) ;
        }

        // Object ctl = controller.newInstance(); //改成使用Bean工厂
        Object ctl = BeanFactory.getBean(controller);
        //参数准备完
        Object res = method.invoke(ctl, parameters); //调用handler中的method方法

        //将方法返回的结果传回到响应response中
        response.getWriter().println(res.toString());
        return true ;
    }
}
