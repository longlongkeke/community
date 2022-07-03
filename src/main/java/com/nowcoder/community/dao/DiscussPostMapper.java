package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author longke
 * @description
 * @create 2022/6/29 15:25
 */
@Mapper
public interface DiscussPostMapper {
    // 考虑到后期分页功能加入offset 和 limit变量
    // @Param注解用于给参数取别名,
    // 如果只有一个参数,并且在<if>里使用,则必须加别名.

    List<DiscussPost> selectDiscussPosts(@Param("userId") int userId,@Param("offset") int offset,@Param("limit") int limit);
    int selectDiscussPostRows(@Param("userId") int userId);//给参数取别名，如果只有一个参数，并且在《if》里使用，则必须加别名。
    int insertDiscussPost(DiscussPost discussPost);
    DiscussPost selectDiscussPostById(int id);
    int updateCommentCount(@Param("id") int id,@Param("commentCount") int commentCount);
}
