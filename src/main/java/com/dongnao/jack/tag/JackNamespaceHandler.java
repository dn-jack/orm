package com.dongnao.jack.tag;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class JackNamespaceHandler extends NamespaceHandlerSupport {
    
    public void init() {
        this.registerBeanDefinitionParser("orm", new JackBeanDefinitionParser());
    }
}
