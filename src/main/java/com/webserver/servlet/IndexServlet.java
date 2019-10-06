package com.webserver.servlet;

import com.webserver.core.Request;
import com.webserver.core.Response;
import com.webserver.core.Servlet;

import java.io.IOException;

/**
 * @Author rainc
 * @create 2019/10/6 17:07
 */
public class IndexServlet implements Servlet {

    @Override
    public void service(Request request, Response response) {
        String uname = request.getParameter("uname");
        String pwd = request.getParameter("pwd");
        if (uname.equals("rainc") && pwd.equals("123456")) {
            response.print("你好" + request.getParameter("uname")/*通过键获取参数值*/);
        } else {
            response.print("账号或密码错误");
        }
    }
}
