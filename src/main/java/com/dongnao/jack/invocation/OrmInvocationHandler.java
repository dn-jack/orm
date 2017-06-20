package com.dongnao.jack.invocation;

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
        return null;
    }
    
    private Object select(XmlTemplate xt, ResultMapBean rmb) {
        
        String sql = xt.getContent();
        PooledConnection con = pool.getConnection();
        ResultSet rs = con.queryBySql(sql);
        Object obj = ResultSetHandler.hand(xt, rmb, rs);
        return obj;
    }
    
}
