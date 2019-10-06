package com.webserver.core;

import java.util.HashSet;
import java.util.Set;

/**
 * <servlet-mapping>
 *     <servlet-name>index</servlet-name>
 *     <url-pattern>/index</url-pattern>
 * </servlet-mapping>
 * @Author rainc
 * @create 2019/10/6 17:56
 */
public class Mapping {
    private String name;
    private Set<String> patterns;

    public Mapping() {
        patterns = new HashSet<>();
    }

    public Mapping(String name, Set<String> pattens) {
        this.name = name;
        this.patterns = pattens;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getPattens() {
        return patterns;
    }

    public void setPattens(Set<String> pattens) {
        this.patterns = pattens;
    }

    public void addPattern(String pattern) {
        this.patterns.add(pattern);
    }

}