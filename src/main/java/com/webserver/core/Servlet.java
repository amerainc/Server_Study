package com.webserver.core;

/**
 * servlet接口，统一servlet类标准
 * @Author rainc
 * @create 2019/10/6 17:02
 */
public interface Servlet {
    void service(Request request, Response response);
}
