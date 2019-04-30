package com.nowcoder.wenda.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * @author jhc on 2019/4/26
 */
@Service
public class JedisAdapter implements InitializingBean {
    private JedisPool jedisPool;
    private static Logger logger = LoggerFactory.getLogger(JedisAdapter.class);
    @Override
    public void afterPropertiesSet() throws Exception {
        jedisPool = new JedisPool("redis://localhost:6379/10");
    }

    public long sadd(String key,String value){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.sadd(key,value);
        }catch (Exception e){
            logger.error("添加异常："+e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    public long scard(String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.scard(key);
        }catch (Exception e){
            logger.error("统计总数异常："+e.getMessage());
        }finally {
            if(jedis != null){
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
            logger.error("添加异常："+e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    public boolean sismember(String key,String value){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.sismember(key,value);
        }catch (Exception e){
            logger.error("添加异常："+e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return false;
    }

    public long lpush(String key,String value){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            long i = jedis.lpush(key,value);
            return i;
        }catch (Exception e){
            logger.error("列表添加异常"+e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    /**
     * brpop 这个方法是阻塞提取出列表最右边的值，如果没有值的话就阻塞，得到的值是一个由 key和value组成的List
     * @param timeout
     * @param key
     * @return
     */
    public List<String> brpop(int timeout,String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.brpop(timeout,key);

        }catch (Exception e){
            logger.error("阻塞提取内容失败"+e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return null;
    }



}
