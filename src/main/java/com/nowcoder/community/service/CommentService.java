package com.nowcoder.community.service;

import com.nowcoder.community.controller.LoginController;
import com.nowcoder.community.dao.CommentMapper;
import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.SensitiveFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author longke
 * @description
 * @create 2022/7/1 10:36
 */
@Service
public class CommentService implements CommunityConstant{
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private SensitiveFilter sensitiveFilter;
    @Autowired
    private DiscussPostService discussPostService;
    public List<Comment> findCommentsByEntity(int entityType,int entityId,int offset,int limit){
        logger.debug("entityType:{}",entityType);
        return commentMapper.selectCommentsByEntity(entityType,entityId,offset,limit);
    }
    public int findCommentCount(int entityType,int entityId){
        return commentMapper.selectCountByEntity(entityType,entityId);
    }
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public int insertComment(Comment comment){
        if(comment==null){
            throw new IllegalArgumentException("参数不能为空");
        }
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveFilter.filter(comment.getContent()));

        int rows = commentMapper.insertComment(comment);
        //更新帖子评论数量
        if(comment.getEntityType()==Entity_TYPE_POST){
             int count = commentMapper.selectCountByEntity(comment.getEntityType(), comment.getEntityId());
            System.out.println("CommentService 查询帖子评论数量的结果："+count);
            int res = discussPostService.updateCommentCount(comment.getEntityId(), count);
            System.out.println("CommentService 更新帖子评论数量的结果："+res);

        }
        return rows;
    }

}
