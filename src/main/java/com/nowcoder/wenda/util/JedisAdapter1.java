package com.nowcoder.wenda.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author jhc on 2019/4/26
 */
@Component
public class JedisAdapter1 implements InitializingBean {
    private static Logger logger = LoggerFactory.getLogger(JedisAdapter1.class);
    private JedisPool jedisPool = null;
    @Override
    public void afterPropertiesSet() throws Exception {
        jedisPool = new JedisPool("redis://localhost:6379/9");
    }

    public long sadd(String key,String value){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.sadd(key,value);
        }catch (Exception e){
            logger.error("添加内容失败"+e.getMessage());
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return 0;
    }

    public long srem(String key,String value){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.srem(key,value);

        }catch (Exception e){
            logger.error("删除内容失败"+e.getMessage());
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return 0;
    }

    public long scard(String key){
        Jedis jedis = null;
        try{
            System.out.println("resource");
            jedis = jedisPool.getResource();
            System.out.println("scard:"+key);
            long i = jedis.scard(key);
            System.out.println(i);
            return i;
        }catch(Exception e){
            logger.error("获取总数目失败: "+e.getMessage());
        }finally {
            if(jedis!=null){
                jedis.close();
            }

        }
        return 0;
    }

    public boolean sismember(String key,String value){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            System.out.println(key+"-"+value);
            boolean s = jedis.sismember(key,value);
            System.out.println(s);
            return s;
        }catch (Exception e){
            logger.error("判断集合当中有没有这个key:" + e.getMessage());
        }finally {
            if(jedis == null){
                jedis.close();
            }
        }
        return false;
    }

//    public List<String> brpop(int timeout,String key){
//
//    }
}
