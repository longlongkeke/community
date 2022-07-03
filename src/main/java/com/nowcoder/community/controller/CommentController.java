package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

/**
 * @author longke
 * @description
 * @create 2022/7/2 22:38
 */
@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private HostHolder hostHolder;
    @RequestMapping(path = "/add/{discussPostId}" ,method = RequestMethod.POST)
    public String addcomment(@PathVariable("discussPostId") int discussPostId,Comment comment){
        System.out.println("comment.getEntityType():"+comment.getEntityType());
        System.out.println("comment.getTargetId():"+comment.getTargetId());
        comment.setUserId(hostHolder.getUserThreadLocal().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.insertComment(comment);
        return "redirect:/discuss/detail/"+discussPostId;
    }
}
