package com.webserver.core;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;

/**
 * 响应处理
 *
 * @Author rainc
 * @create 2019/10/6 15:15
 */
public class Response {
    BufferedWriter bw;
    //正文
    private StringBuilder content;
    //协议头(状态行与请求头 回车)信息
    private StringBuilder responseInfo;
    //协议头返回的
    private int len = 0;
    //通过BLACK来表示一个空格
    private final String BLACK = " ";
    //通过CRLF来表示回车加换行
    private final String CRLF = "\r\n";

    private Response() {
        content = new StringBuilder();
        responseInfo = new StringBuilder();
    }

    public Response(Socket client) {
        //回调无参构造器进行初始化
        this();
        try {
            bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        } catch (IOException e) {
            responseInfo = null;
        }
    }


    //动态添加内容
    public Response print(String info) {
        content.append(info);
        len += info.getBytes().length;
        return this;
    }

    //动态添加内容并且换行
    public Response println(String info) {
        content.append(info).append(CRLF);
        len += (info + CRLF).getBytes().length;
        return this;
    }

    //推送响应信息
    public void pushToBrower(int code) throws IOException {
        //如果错误则返回505错误
        if (responseInfo == null) {
            code = 505;
        }
        //构建头信息
        createResponseInfo(code);
        //放入头信息
        bw.append(responseInfo);
        System.out.println(responseInfo);
        //放入正文信息
        bw.append(content);
        //刷新输出流
        bw.flush();
    }

    //构建头信息
    private void createResponseInfo(int code) {
        //1.响应行：HTTP/1.1 200 OK 响应的状态码 200表示正常应答
        responseInfo.append("HTTP/1.1").append(BLACK);
        responseInfo.append(code).append(BLACK);
        switch (code) {
            case 200:
                responseInfo.append("OK").append(CRLF);
                break;
            case 404:
                responseInfo.append("NOT FOUND").append(CRLF);
                break;
            case 505:
                responseInfo.append("SERVER ERROR").append(CRLF);
        }

        //2、响应头（最后一行存在空行）
        //Date:   生成消息的具体时间和日期
        responseInfo.append("Date:").append(new Date()).append(CRLF);
        //Server：apache  生成服务器的名称
        responseInfo.append("Server:").append("apache").append(CRLF);
        //Content-Type: text/html;charset=utf-8   http服务器告诉浏览器自己响应的对象类型和字符集（并且告诉客户端实际返回的内容的内容类型）
        responseInfo.append("Content-Type:").append("text/html;charset=utf-8").append(CRLF);
        //Content-Length:   http服务器的响应实体正文的长度
        responseInfo.append("Content-length:").append(len).append(CRLF);//必须过去字节长度
        //以回车换行标记结束
        responseInfo.append(CRLF);
    }
}