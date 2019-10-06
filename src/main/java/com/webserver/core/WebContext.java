package com.webserver.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Web上下文统一管理
 *
 * @Author rainc
 * @create 2019/10/6 18:00
 */
public class WebContext {
    List<Entity> entities;
    List<Mapping> mappings;
    //key-->servlet-name value-->serblet-class
    private Map<String, String> entityMap = new HashMap<>();
    //key-->url-pattern value-->servlet-name
    private Map<String, String> mappingMap = new HashMap<>();

    public WebContext(List<Entity> entities, List<Mapping> mappings) {
        this.entities = entities;
        this.mappings = mappings;
        //将entityList转成了对应的map
        for (Entity temp : entities) {
            entityMap.put(temp.getName(), temp.getClz());
        }
        //将mappingsList转成了对应的map
        for (Mapping temp : mappings) {
            for (String st : temp.getPattens()) {
                mappingMap.put(st, temp.getName());
            }
        }
    }

    /**
     * 通过传入的路径得到对应的名字，再通过名字找到对应的class路径
     *
     * @param pattern
     * @return
     */
    public String getClz(String pattern) {
        String name = mappingMap.get(pattern);
        return entityMap.get(name);
    }
}
