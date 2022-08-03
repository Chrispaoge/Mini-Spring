package com.ycj.web.server;

import com.ycj.web.servlet.DispatcherServlet;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

public class TomcatServer {
    private Tomcat tomcat ;
    private String[] args ;

    public TomcatServer(String[] args){
        this.args = args ;
    }

    //创建一个实例对象并调用 start 方法就可以很容易启动 Tomcat
    public void startServer() throws LifecycleException {
        tomcat = new Tomcat() ;
        tomcat.setPort(6699); //设置端口
        tomcat.start();

        //真正管理servlet的容器是context容器
        Context context = new StandardContext() ; //一个context容器对应一个web项目
        context.setPath(""); //为context设置路径
        context.addLifecycleListener(new Tomcat.FixContextListener());//生命周期监听器。注册默认的监听器

        //将创建的TestServlet实例化，注册到此context容器内。后面的.setAsyncSupported(true)是设置支持异步
        DispatcherServlet servlet = new DispatcherServlet() ;
        Tomcat.addServlet(context, "dispatcherServlet", servlet).setAsyncSupported(true); //testServlet是为这个servlet取的名字

        //添加servlet到URI的映射，这样当我们访问此URI的时候，tomcat会调用此servlet
        context.addServletMappingDecoded("/", "dispatcherServlet");

        //由于tomcat的context容器需要依附在一个host容器内，将其注册到默认的host容器
        //这样，tomcat和servlet的联系就建立好了
        tomcat.getHost().addChild(context);

        //为了防止服务器中途退出，添加一个常驻线程
        Thread awaitThread = new Thread(()->{
            TomcatServer.this.tomcat.getServer().await(); //tomcat对象一直在等待
        }, "tomcat_await_thread") ;
        awaitThread.setDaemon(false); //将此线程设置为非守护线程
        awaitThread.start();
    }

}
