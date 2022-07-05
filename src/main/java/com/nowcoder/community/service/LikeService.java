package com.nowcoder.community.service;

import com.nowcoder.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author longke
 * @description
 * @create 2022/7/3 18:09
 */
@Service
public class LikeService {
    @Autowired
    private RedisTemplate redisTemplate;
    public void like(int userId,int entityId,int entityType){
        String key = RedisKeyUtil.getEntityLikeKey(entityId, entityType);
        Boolean ismember = redisTemplate.opsForSet().isMember(key, userId);
        if(ismember){
            redisTemplate.opsForSet().remove(key,userId);

        }else{
            redisTemplate.opsForSet().add(key,userId);
        }

    }
    //查询某实体的点赞数量
    public long findEntityLikeCount(int entityId,int entityType ){
        String key = RedisKeyUtil.getEntityLikeKey(entityId, entityType);
        return redisTemplate.opsForSet().size(key);
    }
    //查询某人对某实体点赞与否
    public int findEntityLikeStatus(int userId,int entityId,int entityType){
        String key = RedisKeyUtil.getEntityLikeKey(entityId, entityType);
        return redisTemplate.opsForSet().isMember(key,userId)?1:0;
    }
}
