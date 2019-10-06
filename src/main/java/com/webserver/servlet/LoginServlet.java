package com.webserver.servlet;

import com.webserver.core.Request;
import com.webserver.core.Response;
import com.webserver.core.Servlet;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Author rainc
 * @create 2019/10/6 19:13
 */
public class LoginServlet implements Servlet {
    @Override
    public void service(Request request, Response response) {
        try {
            //读取login.html的内容并使用response输出到浏览器端
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("login.html");
            response.print(new String(is.readAllBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
