package com.nowcoder.community.service;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author longke
 * @description
 * @create 2022/6/29 15:52
 */
@Service
public class DiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private SensitiveFilter sensitiveFilter;
    public List<DiscussPost> findDiscussPosts(int userId,int offset,int limit){
        return discussPostMapper.selectDiscussPosts(userId,offset,limit);
    }
    public int findDiscussRows(int userId){
        return discussPostMapper.selectDiscussPostRows(userId);
    }
    public int insertDiscussPost(DiscussPost post){
        if(post==null){
            throw  new IllegalArgumentException("参数不能为空");

        }else{
            //转义html标记
            post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
            post.setContent(HtmlUtils.htmlEscape(post.getContent()));
            post.setTitle(sensitiveFilter.filter(post.getTitle()));
            post.setContent(sensitiveFilter.filter(post.getContent()));
            return discussPostMapper.insertDiscussPost(post);

        }
    }
    public DiscussPost findDiscussPostById(int id){
        return discussPostMapper.selectDiscussPostById(id);
    }
    public int updateCommentCount(int id,int commentCount){
        System.out.println("discussPostService 的updateCommentCount："+commentCount);
        return discussPostMapper.updateCommentCount(id,commentCount);
    }


}
