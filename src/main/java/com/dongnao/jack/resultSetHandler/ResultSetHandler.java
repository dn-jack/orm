package com.dongnao.jack.resultSetHandler;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dongnao.jack.mapperTemplate.ResultBean;
import com.dongnao.jack.mapperTemplate.ResultMapBean;
import com.dongnao.jack.mapperTemplate.XmlTemplate;

public class ResultSetHandler {
    
    public static Object hand(XmlTemplate xt, ResultMapBean rmb, ResultSet rs) {
        
        String type = rmb.getType();
        List<ResultBean> results = rmb.getResults();
        try {
            Class<?> clazz = Class.forName(type);
            List<Object> resultList = new ArrayList<Object>();
            while (rs.next()) {
                Object obj = clazz.newInstance();
                for (ResultBean bean : results) {
                    Object value = rs.getObject(bean.getColumn());
                    Field field = clazz.getField(bean.getProperty());
                    
                    TypeHandler(obj, field, value);
                }
                
                resultList.add(obj);
            }
            
            return resultList;
        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    private static void TypeHandler(Object obj, Field field, Object value) {
        try {
            field.setAccessible(true);
            if (field.getType() == Integer.class) {
                field.set(obj, (Integer)value);
            }
            else if (field.getType() == String.class) {
                field.set(obj, (String)value);
            }
        }
        catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
