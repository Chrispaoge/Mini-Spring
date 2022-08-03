package com.ycj.web.servlet;

import com.ycj.web.handler.HandlerManager;
import com.ycj.web.handler.MappingHandler;

import javax.servlet.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class DispatcherServlet implements Servlet {
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    //从servletRequest中读取数据，将结果写入servletResponse中。
    @Override
    public void service(ServletRequest request, ServletResponse response) throws IOException {
        //当一个请求过来时，依次判断框架内的handler能否处理这些请求。能处理就响应结果
        for (MappingHandler mappingHandler : HandlerManager.mappingHandlerList) {
            try {
                if (mappingHandler.handle(request, response)){
                    return ;
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
