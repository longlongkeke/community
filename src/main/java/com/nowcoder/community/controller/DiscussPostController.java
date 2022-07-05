package com.nowcoder.community.controller;

import com.nowcoder.community.dao.CommentMapper;
import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.apache.ibatis.annotations.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author longke
 * @description
 * @create 2022/6/30 21:41
 */
@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant{

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private LikeService likeService;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private HostHolder hostHolder;
    @RequestMapping(path = "/add",method = RequestMethod.POST )
    @ResponseBody
    public String addDiscussPost(String title,String content){
        Integer.valueOf("ab");
        User user = hostHolder.getUserThreadLocal();
        if(user==null){
            return CommunityUtil.getJSONString(403,"您没有登录，请登录！");
        }
        DiscussPost Post=new DiscussPost();
        Post.setUserId(user.getId());
        Post.setTitle(title);
        Post.setContent(content);
        Post.setCreateTime(new Date());
        discussPostService.insertDiscussPost(Post);

        return CommunityUtil.getJSONString(0,"发布成功！");

    }
    @RequestMapping(path = "/detail/{discussPostId}",method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model,Page page){
        DiscussPost Post = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post",Post);
        User user = userService.findUserById(Post.getUserId());
        model.addAttribute("user",user);
        //点赞
        long LikeCount = likeService.findEntityLikeCount(discussPostId,Entity_TYPE_POST);
        int likestatus = likeService.findEntityLikeStatus(hostHolder.getUserThreadLocal().getId(),  discussPostId,Entity_TYPE_POST);
        model.addAttribute("LikeCount",LikeCount);
        model.addAttribute("likestatus",likestatus);

        //评论
        page.setLimit(5);
        page.setPath("/discuss/detail/"+discussPostId);
        page.setTotal(Post.getCommentCount());
        //评论：给帖子的评论
        //回复：给评论的评论
        logger.debug("Entity_TYPE_POST{}:",Entity_TYPE_POST);
        List<Comment> comments = commentMapper.selectCommentsByEntity(Entity_TYPE_POST, Post.getId(), page.getoffset(), page.getLimit());
        //评论VO列表
        List<Map<String,Object>> commentVolist=new ArrayList<>();
        for(Comment comment:comments){
            Map<String,Object> commentvo=new HashMap<>();
            commentvo.put("comment",comment);
            commentvo.put("user",userService.findUserById(comment.getUserId()));

            //点赞

            LikeCount = likeService.findEntityLikeCount( comment.getId(),Entity_TYPE_COMMENT);
            likestatus = likeService.findEntityLikeStatus(hostHolder.getUserThreadLocal().getId(), comment.getId(), Entity_TYPE_COMMENT);
            commentvo.put("LikeCount",LikeCount);
            commentvo.put("likestatus",likestatus);
            //回复列表
            List<Map<String,Object>> replVoList=new ArrayList<>();
            List<Comment> replList = commentMapper.selectCommentsByEntity(Entity_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
            for(Comment repl:replList){
                Map<String,Object> replyVo=new HashMap<>();
                replyVo.put("reply",repl);
                replyVo.put("user",userService.findUserById(repl.getUserId()));
                User target = repl.getTargetId()==0?null:userService.findUserById(repl.getTargetId());
                replyVo.put("target",target);

                //点赞
                LikeCount = likeService.findEntityLikeCount( repl.getId(),Entity_TYPE_COMMENT);
                likestatus = likeService.findEntityLikeStatus(hostHolder.getUserThreadLocal().getId(),  repl.getId(),Entity_TYPE_COMMENT);
                replyVo.put("LikeCount",LikeCount);
                replyVo.put("likestatus",likestatus);
                replVoList.add(replyVo);
            }
            commentvo.put("replys",replVoList);
            int count = commentMapper.selectCountByEntity(Entity_TYPE_COMMENT, comment.getId());
            commentvo.put("replyCount",count);
            commentVolist.add(commentvo);


        }
        model.addAttribute("comment",commentVolist);
        model.addAttribute("offset",page.getoffset());
        return "/site/discuss-detail";


    }


}
