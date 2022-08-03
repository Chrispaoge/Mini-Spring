package com.ycj.beans;

import com.ycj.web.mvc.Controller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//用来初始化和保存Bean
public class BeanFactory {
    //添加一个属性，用来存储Bean类型到Bean实例的映射。这个映射在后续拓展时有可能会并发处理，因此使用ConcurrentHashMap
    private static Map<Class<?>, Object> classToBean = new ConcurrentHashMap<>() ;

    //添加一个方法，用来从此映射中获取Bean
    public static Object getBean(Class<?> cls){
        return classToBean.get(cls) ;
    }

    //Bean初始化。传入之前扫描的类定义，即整个项目的类（test中的）
    public static void initBean(List<Class<?>> classList) throws Exception {
        //由于这些类定义在后续可能还要使用，因此创建一个新的类定义容器toCreate
        List<Class<?>> toCreate = new ArrayList<>(classList) ;
        //循环初始化Bean
        while (toCreate.size() != 0){
            //当容器内还有类定义时，就需要不断的遍历。看类定义能否初始化为Bean。每初始化一个Bean，就从容器内删除
            int remainSize = toCreate.size() ;//保存当前的容器大小
            for (int i = 0 ; i < toCreate.size() ; i ++){
                //完成了创建，就从容器中删除
                if (finishCreate(toCreate.get(i))){
                    toCreate.remove(i) ;
                }
            }
            //类定义容器toCreate每次遍历完后，我们都要判断下，它的大小有没有变化。没有变化，说明陷入了死循环，需要抛出异常
            if (toCreate.size() == remainSize){
                throw new Exception("发生了循环依赖！") ;
            }
        }
    }

    private static boolean finishCreate(Class<?> cls) throws InstantiationException, IllegalAccessException { //初始化Bean成功就返回true
        //先判断是否需要初始化为Bean。如果不需要，直接返回true，然后把它在初始化列表中删除
        //不仅需要判断有无Bean出现，还需判断有无Controller出现。因为Controller也是Bean
        if (!cls.isAnnotationPresent(Bean.class) && !cls.isAnnotationPresent(Controller.class)){
            return true ;
        }

        //初始化Bean
        Object bean = cls.newInstance();

        //对象创建好，解决依赖。遍历下这个类的属性，看看有没有需要解决的依赖
        //如果这个属性被AutoWired注解到，就表示需要使用依赖注入来解决这个依赖
        for (Field field : cls.getDeclaredFields())
            if (field.isAnnotationPresent(AutoWired.class)) {
                //从Bean工厂中获取被依赖的Bean
                Class<?> fieldType = field.getType(); //先拿到这个属性的类型
                Object reliantBean = BeanFactory.getBean(fieldType);//通过类型从Bean工厂中获取Bean
                //如果被依赖的Bean不存在，那么当前Bean肯定会创建失败，直接返回false
                if (reliantBean == null){
                    return false ;
                }

                //如果被依赖的Bean存在的话，就将其注入到创建的Bean中
                field.setAccessible(true);
                field.set(bean, reliantBean); //直接set
            }
        classToBean.put(cls, bean) ; //字段处理完，将这个Bean放到Bean工厂内，并返回true
        return true ;
    }
}
