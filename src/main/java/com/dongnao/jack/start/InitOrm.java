package com.dongnao.jack.start;

import java.util.HashMap;
import java.util.Map;

import com.dongnao.jack.mapperTemplate.ResultMapBean;
import com.dongnao.jack.mapperTemplate.XmlTemplate;
import com.dongnao.jack.xmlparse.XmlParse;

public class InitOrm {
    
    public static Map<String, Object> cache = new HashMap<String, Object>();
    
    static Map<String, Map<String, XmlTemplate>> xmlMap = null;
    
    static Map<String, Map<String, ResultMapBean>> resultMapXmlMap = null;
    
    public static Map<String, Map<String, ResultMapBean>> getResultMapXmlMap() {
        return resultMapXmlMap;
    }
    
    public static Map<String, Map<String, XmlTemplate>> getXmlMap() {
        return xmlMap;
    }
    
    public static void init() {
        if (!cache.containsKey("read")) {
            String xmlpackage = ReadConfig.getValue("jack.orm.xml");
            String daopackage = ReadConfig.getValue("jack.orm.dao");
            
            //            AnnotationHandler.init(daopackage);
            //            List<String> hasRepositoryIntfList = AnnotationHandler.getHasRepositoryIntfList();
            XmlParse.init(xmlpackage);
            xmlMap = XmlParse.getXmlMap();
            resultMapXmlMap = XmlParse.getResultMapXmlMap();
            cache.put("read", true);
        }
    }
}
