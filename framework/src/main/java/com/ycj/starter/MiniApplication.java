package com.ycj.starter;

import com.ycj.beans.BeanFactory;
import com.ycj.core.ClassScanner;
import com.ycj.web.handler.HandlerManager;
import com.ycj.web.server.TomcatServer;

import java.util.List;

public class MiniApplication {
    /**
     * 作为框架的入口类，一般传参为：应用(test)的入口类。通过入口类，可以定位到相应的根目录
     * @param cls：应用的入口类
     * @param args：应用入口类启动时的参数数组
     */
    public static void run(Class<?> cls, String[] args){
        System.out.println("Hello Mini-Spring"); //测下应用模块对框架模块的使用是否成功
        // 使用Tomcat服务器。实例化一个TomcatServer，直接启动即可
        TomcatServer tomcatServer = new TomcatServer(args) ;//args为启动参数
        try {
            tomcatServer.startServer();
            List<Class<?>> classList = ClassScanner.scanClasses(cls.getPackage().getName()) ; //使用项目入口类的包

            //初始化Bean
            BeanFactory.initBean(classList);

            //在框架的入口类MiniApplication中，调用HandlerManager，初始化所有的MappingHandler
            //这里所谓的初始化，其实就是找到所有类中带@Controller的类
            HandlerManager.resolveMappingHandler(classList);

            classList.forEach(it-> System.out.println(it.getName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
