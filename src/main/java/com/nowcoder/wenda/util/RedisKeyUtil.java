package com.nowcoder.wenda.util;

/**
 * @author jhc on 2019/4/26
 */
public class RedisKeyUtil {
    private static String separator = ":";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIEK = "DISLIKE";

    private static String BIZ_QUEUE = "EVENT_QUEUE";

    private static String BIZ_FOLLOWER = "EVENT_FOLLOWER";


    private static String BIZ_FOLLOWEE = "EVENT_FOLLOWEE";

    private static String BIZ_TIMELINE = "EVENT_TIMELINE";

    public static String getLikeKey(int entityType, int entityId) {
        return BIZ_LIKE + separator + String.valueOf(entityType) + separator + String.valueOf(entityId);
    }

    public static String getDislikeKey(int entityType, int entityId) {
        return BIZ_DISLIEK + separator + String.valueOf(entityType) + separator + String.valueOf(entityId);
    }

    public static String getQueueKey() {
        return BIZ_QUEUE;
    }

    public static String getFollowerKey(int entityType, int entityId) {
        return BIZ_FOLLOWER + separator + String.valueOf(entityType) + separator + String.valueOf(entityId);
    }

    public static String getFolloweeKey(int userId, int entityType) {
        return BIZ_FOLLOWEE + separator + String.valueOf(userId) + separator + String.valueOf(entityType);
    }

    public static String getTimelineKey(int userId){
        return BIZ_TIMELINE+separator+String.valueOf(userId);
    }
}
