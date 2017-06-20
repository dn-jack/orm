package com.dongnao.jack.tag;

import java.lang.reflect.Proxy;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

import com.dongnao.jack.dao.SqlMapTemplate;
import com.dongnao.jack.invocation.OrmInvocationHandler;

public class JackBeanDefinitionParser extends
        AbstractSingleBeanDefinitionParser {
    
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        String interfaceStr = element.getAttribute("interface");
        
        Object proxy = getProxy(interfaceStr);
        builder.addPropertyValue("daoInstance", proxy);
    }
    
    protected Class<?> getBeanClass(Element element) {
        return SqlMapTemplate.class;
    }
    
    @Override
    protected String getBeanClassName(Element element) {
        // TODO Auto-generated method stub
        return super.getBeanClassName(element);
    }
    
    private Object getProxy(String interfaceStr) {
        try {
            Class<?> intfClazz = Class.forName(interfaceStr);
            return Proxy.newProxyInstance(intfClazz.getClassLoader(),
                    new Class<?>[] {intfClazz},
                    new OrmInvocationHandler(intfClazz));
        }
        catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return null;
    }
}
