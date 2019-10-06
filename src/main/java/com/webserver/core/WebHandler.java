package com.webserver.core;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * web.xml处理器
 * @Author rainc
 * @create 2019/10/6 18:01
 */
class WebHandler extends DefaultHandler {
    //创建容器保存全部entity和mapping
    private List<Entity> entities;
    private List<Mapping> mappings;
    private Entity entity;
    private Mapping mapping;
    //保存xml的标记
    private String tag;
    //用来判断是映射还是实体
    private boolean isMapping = false;
    //对外提供get方法
    public List<Entity> getEntities() {
        return entities;
    }

    public List<Mapping> getMappings() {
        return mappings;
    }

    //开始解析
    @Override
    public void startDocument() {
        entities = new ArrayList<>();
        mappings = new ArrayList<>();

    }

    //开始接收某个元素
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        tag = qName;
        //通过标记来判断是映射还是实体并生成相应的对象
        switch (tag) {
            case "servlet":
                entity = new Entity();
                isMapping = false;
                break;
            case "servlet-mapping":
                mapping = new Mapping();
                isMapping = true;
            default:
                break;
        }
    }

    //解析元素中内容
    @Override
    public void characters(char[] ch, int start, int length) {
        String contents = new String(ch, start, length).trim();
        //判断是映射还是实体并对相应的对象赋值
        if (!isMapping) {
            switch (tag) {
                case "servlet-name":
                    entity.setName(contents);
                    break;
                case "servlet-class":
                    entity.setClz(contents);
            }
        } else {
            switch (tag) {
                case "servlet-name":
                    mapping.setName(contents);
                    break;
                case "url-pattern":
                    mapping.addPattern(contents);
            }
        }
    }

    //结束某个元素接收
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        //判断实体还是映射保存至容器中
        switch (qName) {
            case "servlet":
                entities.add(entity);
                break;
            case "servlet-mapping":
                mappings.add(mapping);
            default:
                break;
        }
        //重置tag
        tag = "";
    }

    //结束解析
    @Override
    public void endDocument() throws SAXException {
    }
}