package com.dongnao.jack.tag;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import redis.clients.jedis.Jedis;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;

public class Test {
    
    public static void main(String[] args) {
        ApplicationContext app = new ClassPathXmlApplicationContext(
                "config/spring/my.xml");
        System.out.println(app);
        MongoClient client = (MongoClient)app.getBean("mongo");
        
        DB db = client.getDB("dn_order");
        DBCollection con = db.getCollection("jack");
        DBObject o = new BasicDBObject();
        o.put("name", "jack1");
        o.put("age", 31);
        WriteResult wr = con.insert(o);
        
        System.out.println(wr.toString());
        
        Jedis jedis = (Jedis)app.getBean("redis");
        System.out.println(jedis);
        System.out.println(jedis.set("name", "jack"));
        jedis.close();
    }
}
