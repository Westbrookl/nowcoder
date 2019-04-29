package com.nowcoder.wenda.service;

import com.nowcoder.wenda.util.JedisAdapter;
import com.nowcoder.wenda.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jhc on 2019/4/26
 */
@Service
public class LikeService {

    @Autowired
    JedisAdapter jedisAdapter;

    public long getLikeCount(int entityType,int entityId){
        String key = RedisKeyUtil.getLikeKey(entityType,entityId);
        return jedisAdapter.scard(key);
    }

    public long getDislikeCount(int entityType,int entityId){
        String key = RedisKeyUtil.getDislikeKey(entityType,entityId);
        return jedisAdapter.scard(key);
    }

    public long getLikeStatus(int userId,int entityType,int entityId){
        String key = RedisKeyUtil.getLikeKey(entityType,entityId);
        if(jedisAdapter.sismember(key,String.valueOf(userId))){
            return 1;
        }
        String disKey  = RedisKeyUtil.getDislikeKey(entityType,entityId);
        return jedisAdapter.sismember(disKey,String.valueOf(userId))?-1:0;
    }

    public long like(int userId,int entityType,int entityId){
        String disKey = RedisKeyUtil.getDislikeKey(entityType,entityId);
        jedisAdapter.srem(disKey,String.valueOf(userId));
        String like = RedisKeyUtil.getLikeKey(entityType,entityId);
        jedisAdapter.sadd(like,String.valueOf(userId));
        return jedisAdapter.scard(like);
    }

    public long dislike(int userId,int entityType,int entityId){
        String like = RedisKeyUtil.getDislikeKey(entityType,entityId);
        jedisAdapter.srem(like,String.valueOf(userId));
        String dislike = RedisKeyUtil.getDislikeKey(entityType,entityId);
        jedisAdapter.sadd(dislike,String.valueOf(userId));
        return jedisAdapter.scard(dislike);
    }


}
