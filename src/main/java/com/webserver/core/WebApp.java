package com.webserver.core;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;

/**
 * @Author rainc
 * @create 2019/10/6 17:51
 */
public class WebApp {
    private static WebContext context;

    static {
        try {
            //1、获取解析工厂
            SAXParserFactory factory = SAXParserFactory.newInstance();
            //2、从解析工厂获取解析器
            SAXParser parse = factory.newSAXParser();
            //3、加载文档 Document 注册处理器
            //4、编写处理器
            WebHandler handler = new WebHandler();
            //5、解析数据
            parse.parse(Thread.currentThread().getContextClassLoader().getResourceAsStream("web.xml"), handler);
            //将数据存入context中进行统一管理
            context = new WebContext(handler.getEntities(), handler.getMappings());
        } catch (Exception e) {
            System.out.println("解析配置文件错误");
        }
    }

    //通过url获取对应的servlet
    public static Servlet getServletFromUrl(String url) {
        //通过url获取class对应的路径名
        String className = context.getClz(url);

        if (className == null)
            return null;
        try {
            //通过路径名反射生成相应的Servlet
            Class clz = Class.forName(className);
            Servlet servlet = (Servlet) clz.getConstructor().newInstance();
            return servlet;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }
}