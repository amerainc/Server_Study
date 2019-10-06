package com.webserver.core;

import java.io.*;
import java.net.Socket;
import java.util.*;

/**
 * 请求处理
 * @Author rainc
 * @create 2019/10/6 11:55
 */
public class Request {
    //请求方式
    private String method;
    //请求url
    private String url;
    //存储参数可能存在一键多值因此值使用了list
    private Map<String, List<String>> parameterMap;
    private BufferedReader br;

    public Request(Socket client) {
        try {
            //初始化输入流
            this.br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            //初始化map
            parameterMap = new HashMap<>();
            //解析请求
            parseRequest();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析请求行
     */
    private void parseRequest() {
        try {
            //读取第一行内容 GET /login?uname=a HTTP/1.1
            String line = br.readLine();
            //如果为空则停止接下来的步骤
            if (line==null)
                return;
            //通过空格拆分为三部分取得请求方式和url
            String[] temp = line.split(" ");
            method = temp[0].trim();
            //通过问号分析是否有传递参数
            if (temp[1].contains("?")) {
                //通过分割问号得到url和参数
                temp = temp[1].split("\\?");
                url = temp[0].trim();
                convertMap(temp[1].trim());
            } else {
                url = temp[1].trim();
            }
            System.out.println("method:" + method);
            System.out.println("url:" + url);
            //如果请求方式为post则读取post请求内容
            if (method.equals("POST"))
                parsePostParams();
            System.out.println(parameterMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析post参数
     */
    private void parsePostParams() {
        String line;
        int len = 0;
        try {
            //请求体与post参数中间有一个空行，长度会被解析成0，这个循环会正好在读取参数前结束
            while ((line = br.readLine()).length() != 0) {
                //若为post方式则请求中会有Content-Length行，通过Content-Length可以得到参数长度并读取
                if (line.contains("Content-Length")) {
                    //对Content-Length行进行拆分得到长度
                    String[] temp = line.split(":");
                    len = Integer.parseInt(temp[1].trim());
                }
            }
            char[] buffers = new char[len];
            br.read(buffers);
            convertMap(new String(buffers));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将参数分割存入map
     */
    private void convertMap(String params) {
        //1、分割字符串&
        String[] keyValues = params.split("&");
        for (String temp : keyValues) {
            //2、再次分割字符串 =
            String[] kv = temp.split("=");
            kv = Arrays.copyOf(kv, 2);
            //获取key和value
            String key = kv[0];
            String value = kv[1] == null ? null : decode(kv[1], "utf-8");
            //存储到map中如果没有该键则创建该键对应的list
            if (!parameterMap.containsKey(key)) {
                parameterMap.put(key, new ArrayList<String>());
            }
            //如果有则直接存入
            parameterMap.get(key).add(value);
        }
    }

    /**
     * 处理中文乱码
     */
    private String decode(String value, String enc) {
        try {
            return java.net.URLDecoder.decode(value, enc);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 对外提供get方法获取
     *
     * @return
     */
    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    /**
     * 通过key获取对应的多个值
     *
     * @param key
     * @return
     */
    public String[] getParameterValues(String key) {
        List<String> values = this.parameterMap.get(key);
        if (null == values || values.size() < 1) {
            return null;
        }
        return values.toArray(new String[0]);
    }

    /**
     * 通过key获取对应的一个值
     *
     * @param key
     * @return
     */
    public String getParameter(String key) {
        //调用getParameterValues，并获取第一个值
        String[] values = getParameterValues(key);
        return values == null ? "" : values[0];
    }
}
