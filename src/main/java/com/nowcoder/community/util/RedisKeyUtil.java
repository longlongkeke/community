package com.nowcoder.community.util;

/**
 * @author longke
 * @description
 * @create 2022/7/3 18:04
 */
public class RedisKeyUtil {
    private static final String SPLIT=":";
    private static final String PRE="like:entity";
    private static final String PRE_kaptcha="kaptcha";
    private static final String PRE_ticket="ticket";
    private static final String PRE_user="user";
    public static String getEntityLikeKey(int entityId,int entityType){
        return PRE+SPLIT+entityType+SPLIT+entityId;
    }
    //登录验证码
    public static String getKaptchakey(String owner){
        return PRE_kaptcha+SPLIT+owner;
    }

    //登录凭证
    public static String getTicketkey(String ticket){
        return PRE_kaptcha+SPLIT+ticket;
    }

    //用户
    public static String getUserkey(int  userId){
        return PRE_kaptcha+SPLIT+userId;
    }

}
