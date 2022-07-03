package com.nowcoder.community.util;

/**
 * @author longke
 * @description
 * @create 2022/6/30 13:40
 */
public interface CommunityConstant {
//    激活成功
    int ACTIVATION_SUCCESS=0;
    /*激活失败*/
    int ACTIVATION_FAIL=1;
    /*重复激活*/
    int ACTIVATION_REPEATE=2;

    /*默认登录凭证超时时间*/
    int DEFAULT_EXPIRED_SECONDS=3600*12;
    /*记住我登录凭证超时时间*/
    int REMEMEBER_EXPIRED_SECONDS=3600*24*100;

    //实体类型：帖子
    int Entity_TYPE_POST=1;

    //实体类型：帖子
    int Entity_TYPE_COMMENT=2;


}
