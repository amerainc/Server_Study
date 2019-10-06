package com.webserver.core;

/**
 * <servlet>
 *     <servlet-name>index</servlet-name>
 *     <servlet-class>com.webserver.servlet.IndexServlet</servlet-class>
 * </servlet>
 * @Author rainc
 * @create 2019/10/6 17:53
 */
public class Entity {
    private String name;
    private String clz;

    public Entity() {
    }

    public Entity(String name, String clz) {
        this.name = name;
        this.clz = clz;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClz() {
        return clz;
    }

    public void setClz(String clz) {
        this.clz = clz;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "name='" + name + '\'' +
                ", clz='" + clz + '\'' +
                '}';
    }
}