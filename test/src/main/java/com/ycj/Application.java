package com.ycj;

import com.ycj.starter.MiniApplication;

public class Application {
    public static void main(String[] args) {
        System.out.println("Hello World");
        MiniApplication.run(Application.class, args); //第一个参数传入应用模块的入口类，这里应用模块test的入口类为Application
    }
}