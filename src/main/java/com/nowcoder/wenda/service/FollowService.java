package com.nowcoder.wenda.service;

import com.nowcoder.wenda.util.JedisAdapter;
import com.nowcoder.wenda.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author jhc on 2019/5/1
 * 主要的功能是 用户关注某个问题
 * 用户取关某个问题
 */
@Service
public class FollowService {
    @Autowired
    JedisAdapter jedisAdapter;

    /**
     * 用户关注 涉及到一个事务
     * 用户关注以后不仅仅是粉丝数目加一
     * 并且该用户关注的内容也相应的加一
     * 两个同时发生
     *
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public boolean follow(int userId, int entityType, int entityId) {
        String follower = RedisKeyUtil.getFollowerKey(entityType, entityId);
        String followee = RedisKeyUtil.getFolloweeKey(userId, entityType);
        Date date = new Date();
        Jedis jedis = jedisAdapter.getJedis();
        Transaction tx = jedisAdapter.multi(jedis);
        tx.zadd(follower, date.getTime(), String.valueOf(userId));
        tx.zadd(followee, date.getTime(), String.valueOf(entityId));
        List<Object> ret = jedisAdapter.exec(tx, jedis);
        return ret.size() == 2 && (Long) ret.get(0) > 0 && (Long) ret.get(1) > 0;
    }

    public boolean unfollow(int userId, int entityType, int entityId) {
        String follower = RedisKeyUtil.getFollowerKey(entityType, entityId);
        String followee = RedisKeyUtil.getFolloweeKey(userId, entityType);
        Jedis jedis = jedisAdapter.getJedis();
        Transaction tx = jedis.multi();
        tx.zrem(follower, String.valueOf(userId));
        tx.zrem(followee, String.valueOf(entityId));
        List<Object> ret = jedisAdapter.exec(tx, jedis);
        return ret.size() == 2 && (Long) ret.get(0) > 0 && (Long) ret.get(1) > 0;
    }

    public List<Integer> getAllFollowers(int entityType, int entityId, int count) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return getListFromSet(jedisAdapter.zrevrange(followerKey, 0, count));
    }

    public List<Integer> getAllFollowers(int entityType, int entityId, int offset, int count) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return getListFromSet(jedisAdapter.zrevrange(followerKey, offset, offset + count));
    }

    public List<Integer> getAllFollowees(int userId, int entityType, int count) {
        String followee = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return getListFromSet(jedisAdapter.zrevrange(followee, 0, count));
    }

    public List<Integer> getAllFollowees(int userId, int entityType, int offset, int count) {
        String followee = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return getListFromSet(jedisAdapter.zrevrange(followee, offset, offset + count));
    }

    private List<Integer> getListFromSet(Set<String> set) {
        List<Integer> list = new ArrayList<Integer>();
        for (String s : set) {
            list.add(Integer.parseInt(s));
        }
        return list;
    }

    public long getFollowerCount(int entityType, int entityId) {
        String key = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return jedisAdapter.zcard(key);
    }

    public long getFolloweeCount(int userId, int entityType) {
        String key = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return jedisAdapter.zcard(key);
    }

    /**
     * 判断一个用户是否为当前的关注
     * 看这个用户id的score
     * 如果为null的话说明没有关注
     * 如果不为null的话说明已经关注了
     *
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public boolean isFollower(int userId, int entityType, int entityId) {
        String key = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return jedisAdapter.zscore(key, String.valueOf(userId)) != null;
    }
}
