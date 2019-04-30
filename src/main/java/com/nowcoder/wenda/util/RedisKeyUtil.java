package com.nowcoder.wenda.util;

/**
 * @author jhc on 2019/4/26
 */
public class RedisKeyUtil {
    private static String separator = ":";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIEK = "DISLIKE";

    private static String BIZ_QUEUE = "EVENT_QUEUE";
    public static String getLikeKey(int entityType,int entityId){
        return BIZ_LIKE+separator+String.valueOf(entityType)+separator+String.valueOf(entityId);
    }
    public static String getDislikeKey(int entityType,int entityId){
        return BIZ_DISLIEK+separator+String.valueOf(entityType)+separator+String.valueOf(entityId);
    }

    public static String getQueueKey(){
        return  BIZ_QUEUE;
    }

}
