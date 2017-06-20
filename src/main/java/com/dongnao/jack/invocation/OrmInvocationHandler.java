package com.dongnao.jack.invocation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.Map;

import com.dongnao.jack.mapperTemplate.ResultMapBean;
import com.dongnao.jack.mapperTemplate.XmlTemplate;
import com.dongnao.jack.pool.DBManager;
import com.dongnao.jack.pool.JdbcPool;
import com.dongnao.jack.pool.PooledConnection;
import com.dongnao.jack.resultSetHandler.ResultSetHandler;
import com.dongnao.jack.start.InitOrm;

public class OrmInvocationHandler implements InvocationHandler {
    
    public Class<?> intfClazz;
    
    public static JdbcPool pool;
    
    static {
        pool = DBManager.getInstance();
        pool.init();
    }
    
    public OrmInvocationHandler(Class<?> intfClazz) {
        this.intfClazz = intfClazz;
    }
    
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        InitOrm.init();
        Map<String, Map<String, XmlTemplate>> xmlMap = InitOrm.getXmlMap();
        Map<String, Map<String, ResultMapBean>> resultMapXmlMap = InitOrm.getResultMapXmlMap();
        
        if (!xmlMap.containsKey(intfClazz.getName())) {
            throw new RuntimeException("没有定义对应的xml文件！namespace="
                    + intfClazz.getName());
        }
        
        Map<String, XmlTemplate> daoxml = xmlMap.get(intfClazz.getName());
        Map<String, ResultMapBean> resultMapxml = resultMapXmlMap.get(intfClazz.getName());
        
        if (!daoxml.containsKey(method.getName())) {
            throw new RuntimeException("xml中没有定义对应的dao方法！methodName="
                    + method.getName());
        }
        
        XmlTemplate temp = daoxml.get(method.getName());
        
        if (temp.getResultMap() != null) {
            if (!resultMapxml.containsKey(temp.getResultMap())) {
                throw new RuntimeException("xml中没有定义对应的resultMap！resultMap="
                        + temp.getResultMap());
            }
        }
        
        if ("select".equals(temp.getOperTypeStr())) {
            return select(temp, resultMapxml.get(temp.getResultMap()));
        }
        else if ("insert".equals(temp.getOperTypeStr())) {
            return insert(temp, args);
        }
        return null;
    }
    
    private Object select(XmlTemplate xt, ResultMapBean rmb) {
        
        String sql = xt.getContent();
        PooledConnection con = pool.getConnection();
        ResultSet rs = con.queryBySql(sql);
        Object obj = ResultSetHandler.hand(xt, rmb, rs);
        return obj;
    }
    
    private Integer insert(XmlTemplate xt, Object[] args) {
        try {
            String sql = xt.getContent();
            
            String pre = sql.substring(0, sql.indexOf("#"));
            
            String last = sql.substring(sql.indexOf("#") + 1,
                    sql.lastIndexOf("#"));
            last = last.substring(last.indexOf("(") + 1, last.lastIndexOf(")"));
            
            Class<?> clazz = args[0].getClass();
            String[] arry = last.split(",");
            StringBuffer replaceStr = new StringBuffer();
            replaceStr.append("(");
            for (String arr : arry) {
                
                Field field = clazz.getField(arr);
                if (field.getType() == String.class) {
                    replaceStr.append("'" + field.get(args[0]) + "'")
                            .append(",");
                }
                else {
                    replaceStr.append(field.get(args[0])).append(",");
                }
            }
            String str = replaceStr.toString();
            str = str.substring(0, str.lastIndexOf(","));
            str = str + ")";
            
            sql = pre + str;
            
            System.out.println(sql);
            PooledConnection con = pool.getConnection();
            int count = con.updateBySql(sql);
            return count;
        }
        catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
