package com.nowcoder.community.controller;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author longke
 * @description
 * @create 2022/6/29 16:00
 */
@Controller
public class HomeController {
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private UserService userService;

    @RequestMapping(path="/index",method = RequestMethod.GET)
    public String getIndexPage(Model model,Page page ){
//        Integer.valueOf("abc");
        page.setTotal(discussPostService.findDiscussRows(0));
        page.setPath("/index");
        List<DiscussPost> list = discussPostService.findDiscussPosts(0, page.getoffset(), 10);
        List<Map<String,Object>> discussposts=new ArrayList<>();
        if(list!=null){
            for (DiscussPost post:list){
                Map<String,Object> map=new HashMap<>();
                map.put("post",post);
                User user = userService.findUserById(post.getUserId());
                map.put("user",user);
                discussposts.add(map);
            }
        }
        System.out.println(discussposts);
        model.addAttribute("discussPosts",discussposts);
        return "/index";
    }

    @RequestMapping(path="/error",method = RequestMethod.GET)
    public String getErrorPage(){
        return "/error/500";
    }

}
