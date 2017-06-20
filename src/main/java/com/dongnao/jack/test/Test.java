package com.dongnao.jack.test;

import java.util.HashMap;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dongnao.jack.bean.ConsultContent;
import com.dongnao.jack.dao.CommonDao;
import com.dongnao.jack.dao.SqlMapTemplate;

public class Test {
    
    public static void main(String[] args) {
        ApplicationContext app = new ClassPathXmlApplicationContext("orm.xml");
        SqlMapTemplate sqlMap = (SqlMapTemplate)app.getBean("commonDao");
        CommonDao dao = (CommonDao)sqlMap.getTemplate();
        List<ConsultContent> contents = (List<ConsultContent>)dao.queryConsultContent(new HashMap());
        for (ConsultContent content : contents) {
            System.out.print(content.getId() + "\t");
            System.out.print(content.getAreaCode() + "\t");
            System.out.print(content.getContent() + "\t");
            System.out.print(content.getType() + "\t");
            System.out.print(content.getState() + "\t");
            System.out.print(content.getItemIndex() + "\t");
            System.out.print("\n");
        }
        
        ConsultContent content = new ConsultContent();
        content.setContent("Œ““™≤‚ ‘£°113");
        content.setItemIndex(23);
        content.setState(1);
        content.setType("0");
        int count = (Integer)dao.insertConsultContent(content);
        System.out.println(count);
    }
}
