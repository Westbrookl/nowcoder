package com.nowcoder.wenda.sync;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.wenda.util.JedisAdapter;
import com.nowcoder.wenda.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jhc on 2019/4/29
 */
@Service
public class EventProducer {
    @Autowired
    JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel model){
        try{

            String key = RedisKeyUtil.getQueueKey();
            String value = JSONObject.toJSONString(model);
            long l = jedisAdapter.lpush(key,value);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
