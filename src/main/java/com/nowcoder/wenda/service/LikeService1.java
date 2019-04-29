package com.nowcoder.wenda.service;

import com.nowcoder.wenda.util.JedisAdapter1;
import com.nowcoder.wenda.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jhc on 2019/4/26
 */
@Service
public class LikeService1 {
    @Autowired
    JedisAdapter1 jedisAdapter;

    public long getLikeCount(int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        System.out.println(likeKey);
        long count1 = jedisAdapter.scard(likeKey) ;
        System.out.println(count1);
        return count1;
    }

    public long getDislikeCount(int entityType, int entityId) {
        String disLikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);
        return jedisAdapter.scard(disLikeKey);
    }

    public long like(int userId, int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        jedisAdapter.sadd(likeKey, String.valueOf(userId));
        String disLikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);
        jedisAdapter.srem(disLikeKey, String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
    }

    public long disLike(int userId, int entityType, int entityId) {
        String disLikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);
        jedisAdapter.sadd(disLikeKey, String.valueOf(userId));
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        jedisAdapter.srem(likeKey, String.valueOf(userId));
        return jedisAdapter.scard(disLikeKey);
    }

    /**
     * 当返回为1的时候是喜欢 返回-1是不喜欢 返回0表示没有状态
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public int getStatus(int userId, int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        if (jedisAdapter.sismember(likeKey, String.valueOf(userId))) {
            System.out.println("-------");
            return 1;
        }
        System.out.println("+++++++");
        String disLikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);
        System.out.println(disLikeKey);
        int count = jedisAdapter.sismember(disLikeKey, String.valueOf(userId)) ? -1 : 0;
        System.out.println(count);
        return count;
    }


}
