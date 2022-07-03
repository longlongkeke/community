package com.nowcoder.community.dao;

import com.nowcoder.community.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author longke
 * @description
 * @create 2022/6/29 13:15
 */
@Mapper
public interface UserMapper {
    User selectById(int id);

    User selectByName(String username);

    User selectByEmail(String email);

    int insertUser(User user);

    int updateStatus( @Param("id") int id, @Param("status") int status);

    int updateHeader(@Param("id")int id, @Param("headerUrl")String headerUrl);

    int updatePassword(int id, String password);
}
