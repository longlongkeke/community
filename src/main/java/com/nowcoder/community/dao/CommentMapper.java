package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author longke
 * @description
 * @create 2022/7/1 10:17
 */
@Mapper
public interface CommentMapper {
    List<Comment> selectCommentsByEntity(@Param("entityType") int entityType, @Param("entityId") int entityId,@Param("offset") int offset, @Param("limit") int limit);
    int selectCountByEntity(@Param("entityType") int entityType,@Param("entityId") int entityId);
    int insertComment(Comment comment);
}
