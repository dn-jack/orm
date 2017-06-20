package com.dongnao.jack.xmlparse;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.dongnao.jack.mapperTemplate.ResultBean;
import com.dongnao.jack.mapperTemplate.ResultMapBean;
import com.dongnao.jack.mapperTemplate.XmlTemplate;

public class XmlParse {
    
    static List<String> packageNames = new ArrayList<String>();
    
    static List<String> xmlsList = new ArrayList<String>();
    
    /** 
     * @Fields xmlMap 非resultMap形式的xml解析 
     */
    static Map<String, Map<String, XmlTemplate>> xmlMap = new HashMap<String, Map<String, XmlTemplate>>();
    
    /** 
     * @Fields resultMapXmlMap resultMap的xml解析 
     */
    static Map<String, Map<String, ResultMapBean>> resultMapXmlMap = new HashMap<String, Map<String, ResultMapBean>>();
    
    public static Map<String, Map<String, ResultMapBean>> getResultMapXmlMap() {
        return resultMapXmlMap;
    }
    
    public static List<String> getPackageNames() {
        return packageNames;
    }
    
    public static List<String> getXmlsList() {
        return xmlsList;
    }
    
    public static Map<String, Map<String, XmlTemplate>> getXmlMap() {
        return xmlMap;
    }
    
    public static void parse(String path) {
        String uri = replaceTo(path.substring(0, path.lastIndexOf(".")));
        String fileName = uri.substring(uri.lastIndexOf("/") + 1);
        System.out.println("fileName=" + fileName);
        String packageName = uri.replace("/" + fileName, "");
        System.out.println("packageName=" + packageName);
        URL pathUrl = XmlParse.class.getClassLoader().getResource(packageName);
        
        String pathFile = pathUrl.getFile();
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(new File(pathFile + File.separator
                    + fileName + ".xml"));
            Element root = document.getRootElement();
            
            String namespace = root.attribute("namespace").getText();
            xmlMap.put(namespace, parseRoot(root, namespace));
        }
        catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static Map<String, XmlTemplate> parseRoot(Element root,
            String namespace) {
        List elements = root.elements();
        Map<String, XmlTemplate> xmlMap = new HashMap<String, XmlTemplate>();
        for (Iterator it = elements.iterator(); it.hasNext();) {
            Element elm = (Element)it.next();
            String elmName = elm.getName();
            if (!"resultMap".equals(elmName)) {
                String id = elm.attributeValue("id");
                String parameterType = elm.attributeValue("parameterType");
                String resultMap = elm.attributeValue("resultMap");
                String content = elm.getTextTrim();
                
                XmlTemplate xt = new XmlTemplate();
                xt.setOperTypeStr(elmName);
                xt.setId(id);
                xt.setParameterType(parameterType);
                xt.setResultMap(resultMap);
                xt.setContent(content);
                
                xmlMap.put(id, xt);
            }
            else {
                resultMapParse(elm, namespace);
            }
        }
        return xmlMap;
    }
    
    private static void resultMapParse(Element elm, String namespace) {
        String id = elm.attributeValue("id");
        String type = elm.attributeValue("type");
        
        List elements = elm.elements();
        ResultMapBean rmb = new ResultMapBean();
        rmb.setId(id);
        rmb.setType(type);
        for (Iterator it = elements.iterator(); it.hasNext();) {
            Element each = (Element)it.next();
            String column = each.attributeValue("column");
            String property = each.attributeValue("property");
            
            ResultBean rb = new ResultBean();
            rb.setColumn(column);
            rb.setProperty(property);
            rmb.getResults().add(rb);
        }
        Map<String, ResultMapBean> resultMapMap = new HashMap<String, ResultMapBean>();
        resultMapMap.put(id, rmb);
        resultMapXmlMap.put(namespace, resultMapMap);
    }
    
    /** 
     * @Description 扫描packageName，读取xml配置文件
     * @param @param packageName
     * @param @return 参数 
     * @return List<String> 返回类型  
     * @throws 
     */
    public static List<String> scanPackage(String packageName) {
        URL pathUrl = XmlParse.class.getClassLoader()
                .getResource(replaceTo(packageName));
        
        String pathFile = pathUrl.getFile();
        
        File file = new File(pathFile);
        
        System.out.println(pathFile + "是否是文件夹：" + file.isDirectory());
        
        System.out.println(pathFile + "是否是文件：" + file.isFile());
        
        String[] filelist = file.list();
        
        for (String path : filelist) {
            File eachPathFile = new File(pathFile + File.separator + path);
            
            if (eachPathFile.isDirectory()) {
                scanPackage(packageName + "." + path);
            }
            else {
                System.out.println(packageName + "." + eachPathFile.getName());
                packageNames.add(packageName + "." + eachPathFile.getName());
            }
        }
        
        return packageNames;
    }
    
    /** 
     * @Description 过滤xml文件 
     * @param  参数 
     * @return void 返回类型  
     * @throws 
     */
    public static List<String> filterFiles() {
        if (packageNames.size() <= 0) {
            return null;
        }
        
        for (String className : packageNames) {
            if (className.contains(".xml")) {
                xmlsList.add(className);
            }
        }
        
        return xmlsList;
    }
    
    public static void init(String packageName) {
        scanPackage(packageName);
        filterFiles();
        
        for (String xmlpath : xmlsList) {
            parse(xmlpath);
        }
    }
    
    public static String replaceTo(String path) {
        return path.replaceAll("\\.", "/");
    }
}
