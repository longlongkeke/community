package com.nowcoder.community.controller;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author longke
 * @description
 * @create 2022/7/3 18:40
 */
@Controller
public class LikeController {
    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = "/like",method = RequestMethod.POST)
    @ResponseBody
    public String like(int entityType,int entityId){
        System.out.println("EntityType:"+entityType);
        System.out.println("EntityId:"+entityId);
        User user = hostHolder.getUserThreadLocal();
        //点赞
        likeService.like(user.getId(),entityId,entityType);

        long count = likeService.findEntityLikeCount(entityId, entityType);
        int entityLikeStatus = likeService.findEntityLikeStatus(user.getId(), entityId, entityType);
        Map<String,Object> map=new HashMap<>();
        map.put("Likecount",count);
        map.put("LikeStatus",entityLikeStatus);
        return CommunityUtil.getJSONString(0,null,map);
    }
}
