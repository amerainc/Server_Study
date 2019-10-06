package com.webserver.core;

import com.webserver.servlet.IndexServlet;

import java.io.*;
import java.net.Socket;

/**
 * @Author rainc
 * @create 2019/10/5 17:11
 * 分发器，用于详细处理请求并响应
 */
public class Dispatcher implements Runnable {
    private Socket client;
    private Request request;
    private Response response;

    public Dispatcher(Socket client) {
        this.client = client;
        //初始化请求处理
        request = new Request(client);
        //初始化响应处理
        response = new Response(client);
    }

    public void run() {
        //推送
        try {
            //通过映射动态生成Servlet
            Servlet servlet = WebApp.getServletFromUrl(request.getUrl());
            //判断servlet是否存在
            if (null != servlet) {
                //调用service方法进行业务处理
                servlet.service(request, response);
                response.pushToBrower(200);
            } else {
                response.print("网页没有");
                response.pushToBrower(404);
            }
        } catch (IOException e) {
            try {
                response.print("网页炸了");
                response.pushToBrower(500);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        //结束时释放Socket
        relesase();
    }


    /**
     * 释放Socket
     */
    private void relesase() {
        try {
            client.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
