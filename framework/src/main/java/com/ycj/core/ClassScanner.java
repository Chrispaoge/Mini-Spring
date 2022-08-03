package com.ycj.core;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassScanner {
    /**
     * 类扫描的方法。这个方法是一个工具方法且不依赖于任何资源，因此声明为static方法
     * @param packageName：包名
     * @return ：返回扫描到的这个包名下所有的类
     */
    public static List<Class<?>> scanClasses(String packageName) throws IOException, ClassNotFoundException {
        List<Class<?>> classList = new ArrayList<>() ; // 存储扫描到的类
        String path = packageName.replace(".", "/"); // 将包名转换为文件路径

        //使用类加载器，通过路径加载文件
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader(); //获取默认的类加载器
        Enumeration<URL> resources = classLoader.getResources(path) ; //类加载器加载文件，返回值为可遍历的URL资源
        while (resources.hasMoreElements()){ //遍历上面拿到的资源
            URL resource = resources.nextElement(); //获取资源
            if (resource.getProtocol().contains("jar")){ //判断资源类型。由于项目最终打成jar包运行，因此处理下资源类型是jar包的情况
                JarURLConnection jarURLConnection = (JarURLConnection)resource.openConnection();
                String jarFilePath = jarURLConnection.getJarFile().getName() ; //获取jar包的绝对路径(通过jarURLConnection)获取
                //核心：通过jar包的路径，获取jar包下所有的类
                classList.addAll(getClassFromJar(jarFilePath, path)) ;
            }else {
                // todo 上面只处理了资源为jar包的情况，其余的情况还没处理的
            }
        }
        return classList ;
    }

    /**
     * 通过jar包的路径，获取jar包下的类
     * @param jarFilePath:jar包路径
     * @param path：一个jar包下可能有很多类文件，需要指定哪些类文件是我们需要的，可通过类的相对路径来指定
     * @return ：返回获取到的类
     */
    private static List<Class<?>>  getClassFromJar(String jarFilePath, String path)
            throws IOException, ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>() ; //存储类的容器
        JarFile jarFile = new JarFile(jarFilePath) ; //将jar包路径转化为jarFile实例
        Enumeration<JarEntry> jarEntries = jarFile.entries() ; //遍历jar包的所有entry。每个entry都是jar包中的一个文件
        while (jarEntries.hasMoreElements()){ //依次处理jar包中的每个文件
            JarEntry jarEntry = jarEntries.nextElement();
            String entryName = jarEntry.getName();//获取文件的名字。如com/ycj/test/Test.class
            //目标文件为：路径以我们传入路径为开头的文件。同时以.class结尾。这样就可以拿到每个jarEntry所对应的类了
            if (entryName.startsWith(path) && entryName.endsWith(".class")){
                String classFullName = entryName.replace("/", ".").
                        substring(0, entryName.length()-6) ; //获取类的全限定名
                classes.add(Class.forName(classFullName)) ; //类加载器将此类加载到JVM中。Class.forName方法加载
            }
        }
        return classes ; //将获取到的类返回
    }
}
