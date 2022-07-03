package com.nowcoder.community.util;



import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.Map;
import java.util.UUID;

/**
 * @author longke
 * @description
 * @create 2022/6/30 11:07
 */
public class CommunityUtil {
    //生成随机字符串
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }
    //md5 密码加密
    //加盐（后面加随机字符串）
    public static String md5(String key){//key是明文密码＋盐
        if(StringUtils.isBlank(key)){
            return null;//空值，空串
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }
    public static String getJSONString(int code, String msg, Map<String,Object> map){
        JSONObject json = new JSONObject();
        json.put("code",code);
        json.put("msg",msg);
        if(map!=null){
            for(String key:map.keySet()){
                json.put(key,map.get(key));
            }
        }
        return json.toJSONString();
    }
    public static String getJSONString(int code,String msg){
        return getJSONString(code,msg,null);
    }
}
