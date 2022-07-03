package com.nowcoder.community;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
class CommunityApplicationTests {
	@Autowired
	private LoginTicketMapper loginTicketMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private DiscussPostMapper discussPostMapper;
	@Test
	void testSelectUser() {
		User user = userMapper.selectById(101);
		System.out.println(user);
	}
	@Test
	void testInsertUser(){
		User user=new User();
		user.setUsername("test");
		user.setPassword("123456");
		user.setSalt("abc");
		user.setEmail("test@qq.com");
		user.setHeaderUrl("http://images.nowcoder.com/head/23t.png");
		user.setCreateTime(new Date());
		int row = userMapper.insertUser(user);
		System.out.println(row);
		System.out.println(user.getId());
	}
	@Test
	void testupdateUser(){
		int rows = userMapper.updateStatus(150, 1);
		System.out.println(rows);
		  rows = userMapper.updateHeader(150, "http://images.nowcoder.com/head/23t.png");
		System.out.println(rows);

	}
	@Test
	void testselectPosts(){
		List<DiscussPost> list= discussPostMapper.selectDiscussPosts(149, 0, 10);
		for(DiscussPost post:list){
			System.out.println(post);
		}
		int total = discussPostMapper.selectDiscussPostRows(0);
		System.out.println(total);

	}
	@Test
	void testInsertLoginTicket(){
		LoginTicket loginTicket = new LoginTicket();
		loginTicket.setUserId(101);
		loginTicket.setTicket("abc");
		loginTicket.setStatus(0);
		loginTicket.setExpired(new Date(System.currentTimeMillis()+1000*60*10));
		loginTicketMapper.insertLoginTicket(loginTicket);
	}
	@Test
	void testSetectLoginTicket(){
		LoginTicket loginTicket = loginTicketMapper.selectByTicket("abc");
		System.out.println(loginTicket);

		 loginTicketMapper.updateStatus("abc", 1);
		 loginTicket = loginTicketMapper.selectByTicket("abc");
		System.out.println(loginTicket);
	}


}
